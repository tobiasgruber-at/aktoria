import {TestBed} from '@angular/core/testing';

import {VoiceSpeakingService} from './voice-speaking.service';

describe('VoiceSpeakingService', () => {
  let service: VoiceSpeakingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoiceSpeakingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
