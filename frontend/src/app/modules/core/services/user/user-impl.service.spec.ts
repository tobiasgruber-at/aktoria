import { TestBed } from '@angular/core/testing';
import { UserImplService } from './user-impl.service';
import { SharedTestingModule } from '../../../shared/shared-testing.module';

describe('UserImplService', () => {
  let service: UserImplService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserImplService],
      imports: [SharedTestingModule]
    });
    service = TestBed.inject(UserImplService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
