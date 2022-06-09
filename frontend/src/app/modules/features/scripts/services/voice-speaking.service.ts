import {Injectable, OnDestroy} from '@angular/core';
import {ScriptRehearsalService} from './script-rehearsal.service';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {BehaviorSubject, Observable, Subject, takeUntil} from 'rxjs';

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

  constructor(private scriptRehearsalService: ScriptRehearsalService) {
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
      const utter = this.initUtterance(currentLine.content);
      setTimeout(() => {
        this.canceledCurSynth = false;
        this.synth.speak(utter);
        if (this.pausedSubject.getValue()) {
          this.synth.pause();
        }
      }, 10);
    }
  }

  /** Pauses the current synthesis. */
  pauseSpeak() {
    this.pausedSubject.next(true);
    this.synth.pause();
  }

  /** Resumes the current synthesis. */
  resumeSpeak() {
    this.pausedSubject.next(false);
    this.synth.resume();
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

  /** Initializes a synthesis utterance. */
  private initUtterance(content: string): SpeechSynthesisUtterance {
    const utter = new SpeechSynthesisUtterance(content);
    utter.lang = 'de';
    const voice = this.synth.getVoices().find((v) => v.name === 'Anna');
    if (voice) {
      utter.voice = voice;
    }
    utter.addEventListener('end', () => {
      if (!this.canceledCurSynth) {
        this.session.currentLineIndex++;
        this.speakLine();
      }
    });
    return utter;
  }
}
