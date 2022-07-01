import {Component, EventEmitter, HostListener, OnDestroy, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {ScriptRehearsalService} from '../../../services/script-rehearsal.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSession, UpdateSession} from '../../../../../shared/dtos/session-dtos';
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
  @Output() blurEventEmitter = new EventEmitter<boolean>();
  @Output() progressEventEmitter = new EventEmitter<number>();
  @ViewChild('stopSessionModal') stopSessionModal;
  isBlurred = false;
  script: DetailedScript = null;
  session: SimpleSession = null;
  interactionDisabled = false;
  endSessionLoading = false;
  isRecordingMode = false;
  speakingPaused = true;
  lang = 'de';
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private sessionService: SessionService,
    private modalService: NgbModal,
    private router: Router,
    public voiceRecordingService: VoiceRecordingService,
    private toastService: ToastService,
    public voiceSpeakingService: VoiceSpeakingService
  ) {
  }

  /** Whether the current line is spoken by the users role. */
  get isSelectedRoleSpeaking() {
    return this.session
      .getCurrentLine()
      .roles.some((r) => r.name === this.session.role?.name);
  }

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

  // eslint-disable-next-line @typescript-eslint/member-ordering
  @HostListener('window:keyup', ['$event'])
  keyEvent(event: KeyboardEvent) {
    switch (event.key) {
      case 'ArrowDown':
        this.changeLine('next');
        break;
      case 'ArrowUp':
        this.changeLine('prev');
        break;
      case 'ArrowRight':
        this.changeLine('next');
        break;
      case 'ArrowLeft':
        this.changeLine('prev');
        break;
      case 'Enter':
        this.changeLine('next');
        break;
      case 'Space':
        this.pauseUnpause();
        break;
      case 'Escape':
        this.openModal(this.stopSessionModal);
        break;
      default:
        break;
    }
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
    const lines = this.session.getLines();
    const cur = this.session.getCurrentLine().index - lines[0].index;
    const end = lines[lines.length - 1].index - lines[0].index;
    this.progressEventEmitter.emit((cur / end) * 100);

    setTimeout(() => {
      this.interactionDisabled = false;
    }, 300);
  }

  openModal(modal: TemplateRef<any>): void {
    this.modalService.open(modal, {centered: true});
  }

  pauseUnpause() {
    if (this.speakingPaused) {
      this.resumeSpeaking();
    } else {
      this.pauseSpeaking();
    }
  }

  pauseSpeaking() {
    this.voiceSpeakingService.pauseSpeak();
  }

  resumeSpeaking() {
    this.voiceSpeakingService.resumeSpeak();
  }

  blur() {
    this.isBlurred = !this.isBlurred;
    // console.log(this.isBlurred);
    this.blurEventEmitter.emit(this.isBlurred);
  }

  updateLang() {
    this.pauseSpeaking();
    this.voiceSpeakingService.setLang(this.lang);
  }

  /**
   * Toggles the recording mode.
   *
   * @return Promise that resolves once recording started or ended (and properly cached the recording)
   */
  async toggleRecordingMode(): Promise<void> {
    if (!this.isRecordingMode) {
      try {
        await this.scriptRehearsalService.startRecordingMode();
        this.toastService.show({
          message: 'Stimme wird für Texte deiner Rolle aufgezeichnet.',
          theme: Theme.primary
        });
      } catch (err) {
        this.toastService.showError(err);
      }
    } else {
      await this.scriptRehearsalService.stopRecordingMode();
    }
  }

  /** Pauses and closes the rehearsal. */
  pauseSession(modal: NgbActiveModal): void {
    if (this.endSessionLoading) {
      return;
    }
    this.sessionService
      .patchOne(
        this.session.id,
        new UpdateSession(null, null, this.session.getCurrentLine().id)
      )
      .subscribe({
        next: () => {
          modal.dismiss();
          this.cleanUpRehearsal();
          this.router.navigateByUrl(`/scripts/${this.script.id}`);
          this.toastService.show({
            message: 'Lerneinheit beendet.',
            theme: Theme.primary
          });
        },
        error: (err) => {
          this.toastService.showError(err);
        }
      });
  }

  /** Ends the rehearsal and asks for a review. */
  private endSession(): void {
    this.endSessionLoading = true;
    this.sessionService.endSession(this.session.id).subscribe({
      next: async () => {
        await this.cleanUpRehearsal();
        await this.router.navigateByUrl(
          `scripts/${this.script.id}/review/${this.session.id}`
        );
      },
      error: (err) => {
        this.toastService.showError(err);
        this.endSessionLoading = false;
      }
    });
  }

  /** Cleans up the state of the rehearsal. */
  private async cleanUpRehearsal(): Promise<void> {
    if (!this.speakingPaused) {
      this.pauseSpeaking();
    }
    if (this.isRecordingMode) {
      await this.toggleRecordingMode();
    }
  }
}
