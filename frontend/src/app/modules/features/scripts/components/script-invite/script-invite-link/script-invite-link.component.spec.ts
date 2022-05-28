import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptInviteLinkComponent } from './script-invite-link.component';

describe('ScriptInviteLinkComponent', () => {
  let component: ScriptInviteLinkComponent;
  let fixture: ComponentFixture<ScriptInviteLinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptInviteLinkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptInviteLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
