import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptInviteAcceptComponent } from './script-invite-accept.component';

describe('ScriptInviteAcceptComponent', () => {
  let component: ScriptInviteAcceptComponent;
  let fixture: ComponentFixture<ScriptInviteAcceptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptInviteAcceptComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptInviteAcceptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
