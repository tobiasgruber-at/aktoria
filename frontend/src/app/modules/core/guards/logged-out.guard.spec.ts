import {TestBed} from '@angular/core/testing';

import {LoggedOutGuard} from './logged-out-guard.service';

/** Guard to ensure that the user is logged out. */
describe('LoggedOutGuard', () => {
  let guard: LoggedOutGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(LoggedOutGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
