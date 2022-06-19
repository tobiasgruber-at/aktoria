import {Injectable, OnDestroy} from '@angular/core';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {Observable, of, Subject, take} from 'rxjs';
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
  private audioOutputSubject = new Subject<Blob>();
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
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      this.initMediaRecorder(stream);
    } catch (err: any) {
      switch (err.name) {
        case 'NotAllowedError':
          throw new AudioNotAllowedError(
            'Bitte erteile Zugriffsberechtigungen für dein Mikrofon.'
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
  startRecording(): void {
    this.mediaRecorder?.start();
  }

  /**
   * Stops recording.
   *
   * @return Observable of the recorded audio blob.
   */
  stopRecording(): Observable<Blob> {
    if (this.mediaRecorder?.state !== 'recording') {
      return of(null);
    }
    this.mediaRecorder?.stop();
    return this.audioOutputSubject.asObservable().pipe(take(1));
  }

  /** Initializes the media recorder. */
  private initMediaRecorder(stream: MediaStream): void {
    if (this.mediaRecorder) {
      return;
    }
    this.mediaRecorder = new MediaRecorder(stream);
    this.mediaRecorder.onstop = (e) => {
      this.audioOutputSubject.next(
        new Blob(this.chunks, {
          type: 'audio/ogg; codecs=opus'
        })
      );
      this.chunks = [];
    };
    this.mediaRecorder.ondataavailable = (e) => {
      this.chunks.push(e.data);
    };
  }
}
