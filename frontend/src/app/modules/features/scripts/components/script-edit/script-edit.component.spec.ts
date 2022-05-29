import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptEditComponent} from './script-edit.component';

describe('ScriptUploadPreviewComponent', () => {
  let component: ScriptEditComponent;
  let fixture: ComponentFixture<ScriptEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptEditComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
