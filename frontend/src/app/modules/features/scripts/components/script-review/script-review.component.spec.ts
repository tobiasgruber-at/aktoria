import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptReviewComponent } from './script-review.component';

describe('ScriptReviewComponent', () => {
  let component: ScriptReviewComponent;
  let fixture: ComponentFixture<ScriptReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
