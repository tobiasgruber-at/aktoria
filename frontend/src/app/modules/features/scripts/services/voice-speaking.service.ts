import { Injectable, OnDestroy } from '@angular/core';
import { ScriptRehearsalService } from './script-rehearsal.service';
import { SimpleSession } from '../../../shared/dtos/session-dtos';
import { BehaviorSubject, Observable, Subject, takeUntil } from 'rxjs';
import { Theme } from '../../../shared/enums/theme.enum';
import { ToastService } from '../../../core/services/toast/toast.service';
import { SessionService } from '../../../core/services/session/session.service';
import { Router } from '@angular/router';

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
        if (this.curAudioEl.canPlayType(audio) === 'probably' || 'maybe') {
          this.curAudioEl.play();
        }
        if (this.pausedSubject.getValue()) {
          this.curAudioEl.pause();
        }
      } else {
        this.isAutomatedVoiceSpeaking = true;
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
    if (!this.synth.pending) {
      this.speakLine();
    }
    this.pausedSubject.next(false);
    if (this.isAutomatedVoiceSpeaking) {
      this.synth.resume();
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
        if (this.session?.isAtEnd()) {
          this.endSession();
        } else {
          this.session.currentLineIndex++;
          this.speakLine();
        }
      }
    });
    return utter;
  }

  private endSession(): void {
    this.stopSpeak();
    this.sessionService.endSession(this.session.id).subscribe({
      next: () => {
        this.router.navigateByUrl(`/scripts/${this.session.getScriptId()}`);
        this.toastService.show({
          message: 'Lerneinheit abgeschlossen.',
          theme: Theme.primary
        });
      },
      error: (err) => {
        this.toastService.showError(err);
      }
    });
  }
}
