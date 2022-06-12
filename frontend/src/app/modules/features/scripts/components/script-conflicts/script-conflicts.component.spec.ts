import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptConflictsComponent } from './script-conflicts.component';

describe('ScriptConflictsComponent', () => {
  let component: ScriptConflictsComponent;
  let fixture: ComponentFixture<ScriptConflictsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptConflictsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptConflictsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
