import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {ScriptRehearsalService} from '../../../services/script-rehearsal.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';
import {DetailedScript} from '../../../../../shared/dtos/script-dtos';
import {SessionService} from '../../../../../core/services/session/session.service';
import {ToastService} from '../../../../../core/services/toast/toast.service';
import {Theme} from '../../../../../shared/enums/theme.enum';
import {VoiceRecordingService} from '../../../services/voice-recording.service';
import {VoiceSpeakingService} from '../../../services/voice-speaking.service';

/** Control panel for a script rehearsal. */
@Component({
  selector: 'app-rehearsal-controls',
  templateUrl: './rehearsal-controls.component.html',
  styleUrls: ['./rehearsal-controls.component.scss']
})
export class RehearsalControlsComponent implements OnInit, OnDestroy {
  script: DetailedScript = null;
  session: SimpleSession = null;
  interactionDisabled = false;
  endSessionLoading = false;
  isRecordingMode = false;
  speakingPaused = true;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private sessionService: SessionService,
    private modalService: NgbModal,
    private router: Router,
    public voiceRecordingService: VoiceRecordingService,
    private toastService: ToastService,
    public voiceSpeakingService: VoiceSpeakingService
  ) {}

  ngOnInit(): void {
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
    this.scriptRehearsalService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
    this.scriptRehearsalService.$isRecordingMode
      .pipe(takeUntil(this.$destroy))
      .subscribe((isRecordingMode) => {
        this.isRecordingMode = isRecordingMode;
      });
    this.voiceSpeakingService.$paused
      .pipe(takeUntil(this.$destroy))
      .subscribe((paused) => {
        this.speakingPaused = paused;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  changeLine(line: 'next' | 'prev'): void {
    if (this.interactionDisabled || this.endSessionLoading) {
      return;
    }
    this.interactionDisabled = true;
    this.voiceSpeakingService.stopSpeak();
    if (line === 'next') {
      if (this.session?.isAtEnd()) {
        this.endSession();
      } else {
        this.session.currentLineIndex++;
        this.voiceSpeakingService.speakLine();
      }
    } else if (line === 'prev' && !this.session.isAtStart()) {
      this.session.currentLineIndex--;

      this.voiceSpeakingService.speakLine();
    }
    setTimeout(() => {
      this.interactionDisabled = false;
    }, 300);
  }

  openModal(modal: TemplateRef<any>): void {
    this.modalService.open(modal, { centered: true });
  }

  stopSession(modal: NgbActiveModal): void {
    if (this.endSessionLoading) {
      return;
    }
    modal.dismiss();
    this.router.navigateByUrl(`/scripts/${this.script.id}`);
    this.toastService.show({
      message: 'Lerneinheit beendet.',
      theme: Theme.primary
    });
  }

  /** Whether the current line is spoken by the users role. */
  isSelectedRoleSpeaking() {
    return this.session
      .getCurrentLine()
      .roles.some((r) => r.name === this.session.role?.name);
  }

  pause() {
    this.voiceSpeakingService.pauseSpeak();
  }

  resume() {
    this.voiceSpeakingService.resumeSpeak();
  }

  async toggleRecordingMode(): Promise<void> {
    if (!this.isRecordingMode) {
      try {
        await this.scriptRehearsalService.startRecordingMode();
      } catch (err) {
        this.toastService.showError(err);
      }
    } else {
      this.scriptRehearsalService.stopRecordingMode();
    }
  }

  private endSession(): void {
    this.endSessionLoading = true;
    this.sessionService.endSession(this.session.id).subscribe({
      next: () => {
        this.router.navigateByUrl(`/scripts/${this.script.id}`);
        this.toastService.show({
          message: 'Lerneinheit abgeschlossen.',
          theme: Theme.primary
        });
      },
      error: (err) => {
        this.toastService.showError(err);
        this.endSessionLoading = false;
      }
    });
  }
}
