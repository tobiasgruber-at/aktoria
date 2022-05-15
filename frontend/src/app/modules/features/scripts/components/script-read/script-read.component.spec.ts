import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptReadComponent } from './script-read.component';

describe('ScriptPlainComponent', () => {
  let component: ScriptReadComponent;
  let fixture: ComponentFixture<ScriptReadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptReadComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptReadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
