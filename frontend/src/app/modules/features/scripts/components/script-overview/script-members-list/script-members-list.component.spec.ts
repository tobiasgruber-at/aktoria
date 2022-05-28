import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptMembersListComponent } from './script-members-list.component';

describe('ScriptMembersListComponent', () => {
  let component: ScriptMembersListComponent;
  let fixture: ComponentFixture<ScriptMembersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptMembersListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptMembersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
