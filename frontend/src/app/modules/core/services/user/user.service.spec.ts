import { TestBed } from '@angular/core/testing';
import { UserImplService } from './user-impl.service';

describe('UserService', () => {
  let service: UserImplService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserImplService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
