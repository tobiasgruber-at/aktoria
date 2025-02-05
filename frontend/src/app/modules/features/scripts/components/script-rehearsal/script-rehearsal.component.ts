import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { ScriptRehearsalService } from '../../services/script-rehearsal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { SimpleSession } from '../../../../shared/dtos/session-dtos';
import { combineLatest, Subject, takeUntil } from 'rxjs';
import { lineAppearAnimations } from '../../animations/rehearsal-line.animations';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { SessionService } from '../../../../core/services/session/session.service';
import { SectionService } from '../../../../core/services/section/section.service';
import { fixedAppearAnimations } from '../../../../shared/animations/fixed-appear-animations';
import { VoiceSpeakingService } from '../../services/voice-speaking.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

const hasSeenTutorialLSKey = 'hasSeenTutorial';

/** Presents a script rehearsal. */
@Component({
  selector: 'app-script-rehearsal',
  templateUrl: './script-rehearsal.component.html',
  styleUrls: ['./script-rehearsal.component.scss'],
  animations: [lineAppearAnimations, fixedAppearAnimations]
})
export class ScriptRehearsalComponent
  implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('tutorialModal') tutorialModal: ElementRef;
  session: SimpleSession = null;
  getLoading = true;
  isBlurred = false;
  progress = 0;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private route: ActivatedRoute,
    private scriptService: ScriptService,
    private sectionService: SectionService,
    private toastService: ToastService,
    private sessionService: SessionService,
    private router: Router,
    private voiceSpeakingService: VoiceSpeakingService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const scriptId = +params.get('scriptId');
      const rehearsalId = +params.get('rehearsalId');
      const handleNotFound = (err) => {
        this.toastService.showError(err);
        this.router.navigateByUrl('/scripts');
      };
      combineLatest([
        this.scriptService.getOne(scriptId),
        this.sessionService.getOne(rehearsalId)
      ])
        .pipe(takeUntil(this.$destroy))
        .subscribe({
          next: ([script, session]) => {
            this.sectionService
              .getOne(session.sectionId)
              .pipe(takeUntil(this.$destroy))
              .subscribe({
                next: (section) => {
                  // init before it's set
                  session.init(script, section);
                  this.scriptRehearsalService.setScript(script);
                  this.scriptRehearsalService.setSession(session);
                  this.getLoading = false;
                },
                error: handleNotFound
              });
          },
          error: handleNotFound
        });
    });
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
    this.scriptRehearsalService.$selectedRole
      .pipe(takeUntil(this.$destroy))
      .subscribe((role) => {
        // in case the cache was emptied
        if (role === undefined) {
          this.router.navigateByUrl('scripts');
        }
      });
  }

  ngAfterViewInit() {
    const hasSeenTutorial = JSON.parse(
      localStorage.getItem(hasSeenTutorialLSKey)
    );
    if (hasSeenTutorial !== true) {
      this.modalService.open(this.tutorialModal, { centered: true });
    }
  }

  setProgress(progress) {
    this.progress = progress;
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
    this.voiceSpeakingService.stopSpeak();
  }

  blur(event) {
    this.isBlurred = event;
  }
}
