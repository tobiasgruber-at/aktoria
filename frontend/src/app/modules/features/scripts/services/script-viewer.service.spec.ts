import {TestBed} from '@angular/core/testing';

import {ScriptViewerService} from './script-viewer.service';

describe('ScriptViewerService', () => {
  let service: ScriptViewerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScriptViewerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
