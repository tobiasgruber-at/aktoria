import {TestBed} from '@angular/core/testing';

import {VoiceRecordingService} from './voice-recording.service';

describe('VoiceRecordingService', () => {
  let service: VoiceRecordingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoiceRecordingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
