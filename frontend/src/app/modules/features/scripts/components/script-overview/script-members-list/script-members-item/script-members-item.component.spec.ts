import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptMembersItemComponent } from './script-members-item.component';

describe('ScriptMembersItemComponent', () => {
  let component: ScriptMembersItemComponent;
  let fixture: ComponentFixture<ScriptMembersItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptMembersItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptMembersItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
