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
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private sessionService: SessionService,
    private modalService: NgbModal,
    private router: Router,
    public voiceRecordingService: VoiceRecordingService,
    private toastService: ToastService
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
    if (line === 'next') {
      if (this.session?.isAtEnd()) {
        this.endSession();
      } else {
        this.session.currentLineIndex++;
      }
    } else if (line === 'prev' && !this.session.isAtStart()) {
      this.session.currentLineIndex--;
    }
    this.scriptRehearsalService.setSession(this.session);
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
