import {Injectable, OnDestroy} from '@angular/core';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {Subject} from 'rxjs';
import {AudioNotAllowedError, AudioNotSupportedError} from '../errors/audio-errors';

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

  /** @return Whether voice recordings are supported by the browser. */
  isSupported(): boolean {
    return !!navigator.mediaDevices.getUserMedia;
  }

  /**
   * Requests for permission to record audio.
   *
   * @return Promise that resolves with the stream if permissions granted, or rejects otherwise.
   * @throws AudioNotSupportedError If browser doesn't support microphone access
   * @throws AudioNotAllowedError If user rejected microphone access
   */
  async requestPermissions(): Promise<void> {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({audio: true});
      this.initMediaRecorder(stream);
    } catch (err: any) {
      switch (err.name) {
        case 'NotAllowedError':
          throw new AudioNotAllowedError(
            'Bitte aktiviere Berechtigungen für das Mirkofon.'
          );
        case 'NotFoundError':
          throw new AudioNotSupportedError(
            'Sprachaufnahmen werden von deinem Browser nicht unterstützt.'
          );
      }
    }
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
    if (this.mediaRecorder) {
      return;
    }
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
