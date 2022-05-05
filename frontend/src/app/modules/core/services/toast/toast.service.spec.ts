import { TestBed } from '@angular/core/testing';

import { ToastService } from './toast.service';
import { SharedTestingModule } from '../../../shared/shared-testing.module';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SharedTestingModule]
    });
    service = TestBed.inject(ToastService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
