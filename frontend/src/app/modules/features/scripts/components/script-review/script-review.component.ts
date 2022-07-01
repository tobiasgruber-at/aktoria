import {Component, OnDestroy, OnInit} from '@angular/core';
import {ScriptRehearsalService} from '../../services/script-rehearsal.service';
import {Line, Role, SimpleScript} from '../../../../shared/dtos/script-dtos';
import {SimpleSession} from '../../../../shared/dtos/session-dtos';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {SessionService} from '../../../../core/services/session/session.service';
import {SimpleSection} from '../../../../shared/dtos/section-dtos';
import {SectionService} from '../../../../core/services/section/section.service';
import {ScriptViewerService} from '../../services/script-viewer.service';
import {FormBase} from '../../../../shared/classes/form-base';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';
import {Subject, takeUntil} from 'rxjs';
import {LineService} from '../../../../core/services/line/line.service';
import {VoiceSpeakingService} from '../../services/voice-speaking.service';

@Component({
  selector: 'app-script-review',
  templateUrl: './script-review.component.html',
  styleUrls: ['./script-review.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptReviewComponent
  extends FormBase
  implements OnInit, OnDestroy {
  script: SimpleScript;
  session: SimpleSession;
  section: SimpleSection;
  isPlayingLines = false;
  recordedLines: Line[] = [];
  private selectedRole: Role = null;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private route: ActivatedRoute,
    private scriptService: ScriptService,
    private sessionService: SessionService,
    private sectionService: SectionService,
    public scriptViewerService: ScriptViewerService,
    private formBuilder: FormBuilder,
    private toastService: ToastService,
    private router: Router,
    private lineService: LineService,
    private voiceSpeakingService: VoiceSpeakingService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    const handleError = () => {
      this.toastService.show({
        message: 'Deine Lerneinheit ist abgelaufen.',
        theme: Theme.danger
      });
      this.router.navigateByUrl('/scripts');
    };
    this.form = this.formBuilder.group({
      assessment: ['', [Validators.required]],
      shouldSaveRecordings: [false]
    });

    this.scriptRehearsalService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
        this.scriptViewerService.setScript(script);
      });
    /** Gets the recorded lines. */
    const getRecordedLines = () => {
      if (this.session && this.selectedRole) {
        this.recordedLines =
          this.session.getLines()?.filter((l) => {
            const isOwnLine = l.roles.some(
              (r) => r.name === this.selectedRole.name
            );
            return isOwnLine && l.temporaryRecording;
          }) || [];
      }
    };
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        if (session === null) {
          handleError();
          return;
        }
        this.session = session;
        this.scriptViewerService.setMarkedSection({
          section: session.getSection(),
          scrollTo: true
        });
        getRecordedLines();
      });
    this.scriptRehearsalService.$selectedRole
      .pipe(takeUntil(this.$destroy))
      .subscribe((role) => {
        this.selectedRole = role;
        if (role) {
          this.scriptViewerService.setSelectedRole(role);
        }
        getRecordedLines();
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  async playRecording() {
    if (this.isPlayingLines) {
      return;
    }
    this.isPlayingLines = true;
    for (const line of this.recordedLines) {

      this.scriptViewerService.scrollToLine(line.index);
      await this.voiceSpeakingService.speakCustomLine(line);
    }
    this.isPlayingLines = false;
  }

  protected processSubmit(): void {
    const {assessment, shouldSaveRecordings} = this.form.value;
    /** Assesses the session. */
    const assessSession = () => {
      this.sessionService
        .patchOne(this.session.id, {selfAssessment: assessment})
        .pipe(takeUntil(this.$destroy))
        .subscribe({
          next: () => {
            this.sessionService.endSession(this.session.id).subscribe({
              next: () => {
                this.router.navigateByUrl(`/scripts/${this.script.getId()}`);
                this.toastService.show({
                  message: 'Lerneinheit abgeschlossen.',
                  theme: Theme.primary
                });
              },
              error: this.handleError
            });
          },
          error: this.handleError
        });
    };
    if (shouldSaveRecordings && this.recordedLines.length > 0) {
      let savedLinesCounter = 0;
      this.recordedLines.forEach((l) => {
        l.audio = l.temporaryRecording;
        l.temporaryRecording = null;
        l.temporaryRecordingUrl = null;

        const reader = new FileReader();
        reader.readAsDataURL(l.audio);
        reader.onload = () => {
          const audioBase64 = reader.result;
          this.lineService.patchLine({audio: audioBase64}, l.id).subscribe({
            next: () => {
              if (++savedLinesCounter >= this.recordedLines.length) {
                assessSession();
              }
            },
            error: this.handleError
          });
        };
      });
    } else {
      assessSession();
    }
  }
}
