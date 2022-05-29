import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptRehearsalComponent} from './script-rehearsal.component';

describe('ScriptRehearsalComponent', () => {
  let component: ScriptRehearsalComponent;
  let fixture: ComponentFixture<ScriptRehearsalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptRehearsalComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptRehearsalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
