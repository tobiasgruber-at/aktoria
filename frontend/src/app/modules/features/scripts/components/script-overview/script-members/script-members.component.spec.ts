import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptMembersComponent } from './script-members.component';

describe('ScriptMembersComponent', () => {
  let component: ScriptMembersComponent;
  let fixture: ComponentFixture<ScriptMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
