import { inject, TestBed } from '@angular/core/testing';

import { AuthGuard } from './auth.guard';
import { SharedTestingModule } from '../../shared/shared-testing.module';

describe('AuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthGuard],
      imports: [SharedTestingModule]
    });
  });

  it('should ...', inject([AuthGuard], (guard: AuthGuard) => {
    expect(guard).toBeTruthy();
  }));
});
