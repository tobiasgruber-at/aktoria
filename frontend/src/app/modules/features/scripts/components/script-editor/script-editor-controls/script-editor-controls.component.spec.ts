import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptEditorControlsComponent } from './script-editor-controls.component';

describe('ScriptEditorControlsComponent', () => {
  let component: ScriptEditorControlsComponent;
  let fixture: ComponentFixture<ScriptEditorControlsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptEditorControlsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptEditorControlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
