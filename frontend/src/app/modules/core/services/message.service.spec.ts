import { TestBed } from '@angular/core/testing';

import { MessageService } from './message.service';
import { SharedTestingModule } from '../../shared/shared-testing.module';

describe('MessageService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [SharedTestingModule]
    })
  );

  it('should be created', () => {
    const service: MessageService = TestBed.inject(MessageService);
    expect(service).toBeTruthy();
  });
});
