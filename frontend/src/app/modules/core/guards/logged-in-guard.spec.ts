import {inject, TestBed} from '@angular/core/testing';

import {LoggedInGuard} from './logged-in-guard.service';
import {SharedTestingModule} from '../../shared/shared-testing.module';

describe('AuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoggedInGuard],
      imports: [SharedTestingModule]
    });
  });

  it('should ...', inject([LoggedInGuard], (guard: LoggedInGuard) => {
    expect(guard).toBeTruthy();
  }));
});
