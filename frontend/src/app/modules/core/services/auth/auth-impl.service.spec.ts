import {TestBed} from '@angular/core/testing';

import {AuthImplService} from './auth-impl.service';
import {SharedTestingModule} from '../../../shared/shared-testing.module';

describe('AuthImplService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      providers: [AuthImplService],
      imports: [SharedTestingModule]
    })
  );

  it('should be created', () => {
    const service: AuthImplService = TestBed.inject(AuthImplService);
    expect(service).toBeTruthy();
  });
});
