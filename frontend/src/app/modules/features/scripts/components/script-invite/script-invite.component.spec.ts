import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptInviteComponent } from './script-invite.component';

describe('ScriptInviteComponent', () => {
  let component: ScriptInviteComponent;
  let fixture: ComponentFixture<ScriptInviteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptInviteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptInviteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
