import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptUploadReviewComponent } from './script-upload-review.component';

describe('ScriptUploadPreviewComponent', () => {
  let component: ScriptUploadReviewComponent;
  let fixture: ComponentFixture<ScriptUploadReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptUploadReviewComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptUploadReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
