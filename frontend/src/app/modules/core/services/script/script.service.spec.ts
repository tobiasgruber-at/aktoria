import {TestBed} from '@angular/core/testing';

import {ScriptService} from './script.service';

describe('ScriptServiceService', () => {
  let service: ScriptService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScriptService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
