import {Injectable, OnDestroy} from '@angular/core';
import {ScriptRehearsalService} from './script-rehearsal.service';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {Subject, takeUntil} from 'rxjs';

/**
 * Service for recording the role voices of script phrases.<br>
 * Implemented by use of the Media Recording API.
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/API/MediaStream_Recording_API/Using_the_MediaStream_Recording_API
 */
@Injectable()
export class VoiceRecordingService implements OnDestroy {
  private mediaRecorder: MediaRecorder = null;
  /** Chunks of recorded audio. */
  private chunks: Blob[] = [];
  private session: SimpleSession = null;
  private $destroy = new Subject<void>();

  constructor(scriptRehearsalService: ScriptRehearsalService) {
    scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.mediaRecorder?.stop();
        this.session = session;
        this.mediaRecorder?.start();
      });
    scriptRehearsalService.$isRecordingVoice
      .pipe(takeUntil(this.$destroy))
      .subscribe((isRecording) => {
        if (isRecording) {
          this.startRecording();
        } else {
          this.stopRecording();
        }
      });
  }

  /** @return Whether voice recordings are supported by the browser. */
  isSupported(): boolean {
    return !!navigator.mediaDevices.getUserMedia;
  }

  /**
   * Requests for permission to record audio.
   *
   * @return Promise that resolves with the stream if permissions granted, or rejects otherwise.
   * */
  requestPermissions(): Promise<any> {
    return navigator.mediaDevices
      .getUserMedia({audio: true})
      .then((stream) => {
        this.initMediaRecorder(stream);
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  /** Starts recording. */
  private startRecording(): void {
    this.mediaRecorder?.start();
  }

  /** Stops recording. */
  private stopRecording(): void {
    this.mediaRecorder?.stop();
  }

  /** Initializes the media recorder. */
  private initMediaRecorder(stream: MediaStream): void {
    this.mediaRecorder = new MediaRecorder(stream);
    this.mediaRecorder.onstop = (e) => {
      const curLine = this.session.getCurrentLine();
      if (!curLine) {
        // todo: handle error
        return;
      }
      curLine.temporaryRecording = new Blob(this.chunks, {
        type: 'audio/ogg; codecs=opus'
      });
      curLine.temporaryRecordingUrl = window.URL.createObjectURL(
        curLine.temporaryRecording
      );
      /*const audio = document.createElement('audio');
      audio.setAttribute('controls', '');
      audio.controls = true;
      audio.src = curLine.temporaryRecordingUrl;
      audio.play();*/
      this.chunks = [];
    };
    this.mediaRecorder.ondataavailable = (e) => {
      this.chunks.push(e.data);
    };
  }
}
