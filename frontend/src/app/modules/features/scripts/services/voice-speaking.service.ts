import {Injectable, OnDestroy} from '@angular/core';
import {ScriptRehearsalService} from './script-rehearsal.service';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {BehaviorSubject, Observable, Subject, takeUntil} from 'rxjs';
import {ToastService} from '../../../core/services/toast/toast.service';
import {SessionService} from '../../../core/services/session/session.service';
import {Router} from '@angular/router';

/**
 * Service for speaking the roles voices of script phrases.<br>
 * Implemented by use of the SpeechSynthesis API or the recorded audio.
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/API/SpeechSynthesis
 * @author Nik Peter, Tobias Gruber, Luke Nemeskeri
 */
@Injectable()
export class VoiceSpeakingService implements OnDestroy {
  /** @see SpeechSynthesis */
  private synth: SpeechSynthesis = window.speechSynthesis;
  private curAudioEl;
  private isAutomatedVoiceSpeaking = true;
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
    this.stopSpeak();
    const currentLine = this.session.getCurrentLine();
    if (
      currentLine &&
      !currentLine.roles.some((r) => r.name === this.session.role?.name)
    ) {
      if (currentLine.audio) {
        this.isAutomatedVoiceSpeaking = false;
        const audio = window.URL.createObjectURL(currentLine.audio);
        this.curAudioEl = document.createElement('audio');
        this.curAudioEl.setAttribute('controls', '');
        this.curAudioEl.controls = true;
        this.curAudioEl.src = audio;
        this.curAudioEl.onended = this.onSpeakingEnd.bind(this);
        if (this.curAudioEl.canPlayType(audio) === 'probably' || 'maybe') {
          this.curAudioEl.play();
        }
        if (this.pausedSubject.getValue()) {
          this.curAudioEl.pause();
        }
      } else {
        const utter = this.initUtterance(currentLine.content);
        setTimeout(() => {
          this.canceledCurSynth = false;
          if (!this.pausedSubject.getValue()) {
            this.synth.speak(utter);
            this.synth.resume();
          }
        }, 300);
      }
    }
  }

  /** Pauses the current synthesis or audio. */
  pauseSpeak() {
    this.pausedSubject.next(true);
    if (this.isAutomatedVoiceSpeaking) {
      this.synth.pause();
    } else {
      this.curAudioEl.pause();
    }
  }

  /** Resumes the current synthesis or audio. */
  resumeSpeak() {
    this.pausedSubject.next(false);
    if (this.isAutomatedVoiceSpeaking) {
      this.speakLine();
    } else {
      this.curAudioEl.play();
    }
  }

  /** Stops the current synthesis or audio. */
  stopSpeak() {
    this.canceledCurSynth = true;
    if (this.isAutomatedVoiceSpeaking) {
      this.synth.cancel();
    } else {
      this.curAudioEl.pause();
    }
  }

  speakCustomLine(line) {
    this.stopSpeak();
    const currentLine = line;
    if (currentLine) {
      if (currentLine.audio) {
        this.isAutomatedVoiceSpeaking = false;
        const audio = window.URL.createObjectURL(currentLine.audio);
        this.curAudioEl = document.createElement('audio');
        this.curAudioEl.setAttribute('controls', '');
        this.curAudioEl.controls = true;
        this.curAudioEl.src = audio;
        this.curAudioEl.onended = this.onSpeakingEnd.bind(this);
        if (this.curAudioEl.canPlayType(audio) === 'probably' || 'maybe') {
          this.curAudioEl.play();
        }
        if (this.pausedSubject.getValue()) {
          this.curAudioEl.pause();
        }
      } else {
        const utter = this.initUtterance(currentLine.content);
        setTimeout(() => {
          this.canceledCurSynth = false;
          if (!this.pausedSubject.getValue()) {
            this.synth.speak(utter);
            this.synth.resume();
          }
        }, 300);
      }
    }
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
    if (
      utter.lang === 'de' ||
      utter.lang === 'de-AT' ||
      utter.lang === 'de-DE'
    ) {
      utter.voice = this.synth
        .getVoices()
        .find((v) => v.name === 'Microsoft Michael - German (Austria)');
    }

    utter.addEventListener('end', this.onSpeakingEnd.bind(this));
    return utter;
  }

  /** Handles whether next line should be spoken when speaking ended. */
  private onSpeakingEnd(): void {
    if (!this.canceledCurSynth) {
      if (this.session?.isAtEnd()) {
        this.endSession();
      } else {
        this.session.currentLineIndex++;
        this.speakLine();
      }
    }
  }

  private endSession(): void {
    this.stopSpeak();
    this.sessionService.endSession(this.session.id).subscribe({
      next: () => {
        this.router.navigateByUrl(
          `scripts/${this.session.getScriptId()}/review/${this.session.id}`
        );
      },
      error: (err) => {
        this.toastService.showError(err);
      }
    });
  }
}
