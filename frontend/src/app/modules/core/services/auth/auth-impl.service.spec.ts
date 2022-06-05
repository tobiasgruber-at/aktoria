import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { SharedTestingModule } from '../../../shared/shared-testing.module';

describe('AuthImplService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [AuthService],
      imports: [SharedTestingModule]
    })
  );

  it('should be created', () => {
    const service: AuthService = TestBed.inject(AuthService);
    expect(service).toBeTruthy();
  });
});
