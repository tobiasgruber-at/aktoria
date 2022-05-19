import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UploadInformationComponent} from './upload-information.component';

describe('UploadInformationComponent', () => {
  let component: UploadInformationComponent;
  let fixture: ComponentFixture<UploadInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UploadInformationComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
