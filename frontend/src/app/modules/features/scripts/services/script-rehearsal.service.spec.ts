import { TestBed } from '@angular/core/testing';

import { ScriptRehearsalService } from './script-rehearsal.service';

describe('ScriptRehearsalService', () => {
  let service: ScriptRehearsalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScriptRehearsalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
