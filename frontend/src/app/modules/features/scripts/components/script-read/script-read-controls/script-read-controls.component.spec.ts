import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptReadControlsComponent} from './script-read-controls.component';

describe('ScriptReadControlsComponent', () => {
  let component: ScriptReadControlsComponent;
  let fixture: ComponentFixture<ScriptReadControlsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptReadControlsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptReadControlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
