import {Injectable, OnDestroy} from '@angular/core';
import {ScriptRehearsalService} from './script-rehearsal.service';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {BehaviorSubject, Observable, Subject, takeUntil} from 'rxjs';
import {Theme} from '../../../shared/enums/theme.enum';
import {ToastService} from '../../../core/services/toast/toast.service';
import {SessionService} from '../../../core/services/session/session.service';
import {Router} from '@angular/router';

/**
 * Service for speaking the roles voices of script phrases.<br>
 * Implemented by use of the SpeechSynthesis API.
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis
 * @author Nik Peter, Tobias Gruber
 */
@Injectable()
export class VoiceSpeakingService implements OnDestroy {
  /** @see SpeechSynthesis */
  private synth: SpeechSynthesis = window.speechSynthesis;
  /** Whether the currently spoken synthesis should be canceled once it's completed. */
  private canceledCurSynth: boolean;
  private session: SimpleSession;
  private $destroy = new Subject<void>();
  private pausedSubject = new BehaviorSubject<boolean>(true);
  private lang = 'de';

  constructor(
    private scriptRehearsalService: ScriptRehearsalService,
    private toastService: ToastService,
    private sessionService: SessionService,
    private router: Router
              ) {
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
  }

  get $paused(): Observable<boolean> {
    return this.pausedSubject.asObservable();
  }

  /**
   * Speaks the current line.<br>
   * Stops already ongoing voices and only speaks, if the line is not spoken by the user.
   * */
  speakLine() {
    console.log('speak!');
    this.stopSpeak();
    const currentLine = this.session.getCurrentLine();
    if (
      currentLine &&
      !currentLine.roles.some((r) => r.name === this.session.role?.name)
    ) {
      console.log('not me!');
      const utter = this.initUtterance(currentLine.content);
      setTimeout(() => {
        this.canceledCurSynth = false;
        if (!this.pausedSubject.getValue()) {
          console.log('send to api!');
          console.log(utter);
          this.synth.speak(utter);
          this.synth.resume();
        }
      }, 300);
    }
  }

  /** Pauses the current synthesis. */
  pauseSpeak() {
    this.pausedSubject.next(true);
    this.synth.pause();
  }

  /** Resumes the current synthesis. */
  resumeSpeak() {
    this.speakLine();
    this.pausedSubject.next(false);
  }

  /** Stops the current synthesis. */
  stopSpeak() {
    this.canceledCurSynth = true;
    this.synth.cancel();
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
    this.stopSpeak();
  }

  setLang(lang) {
    this.lang = lang;
    this.stopSpeak();
  }

  /** Initializes a synthesis utterance. */
  private initUtterance(content: string): SpeechSynthesisUtterance {
    const utter = new SpeechSynthesisUtterance(content);
    utter.lang = this.lang;
    if (utter.lang === 'de' || utter.lang === 'de-AT' || utter.lang === 'de-DE') {
      utter.voice = this.synth.getVoices().find((v) => v.name === 'Microsoft Michael - German (Austria)');
    }

    utter.addEventListener('end', () => {
      if (!this.canceledCurSynth) {
        if (this.session?.isAtEnd()) {
          this.endSession();
        } else {
          this.session.currentLineIndex++;
          this.speakLine();
        }
      }
    }, true);
    return utter;
  }

  private endSession(): void {
    this.stopSpeak();
    this.sessionService.endSession(this.session.id).subscribe({
      next: () => {
        this.router.navigateByUrl(`scripts/${this.session.getScriptId()}/review/${this.session.id}`);
      },
      error: (err) => {
        this.toastService.showError(err);
      }
    });
  }
}
