import {TestBed} from '@angular/core/testing';
import {UserService} from './user.service';
import {SharedTestingModule} from '../../../shared/shared-testing.module';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService],
      imports: [SharedTestingModule]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
