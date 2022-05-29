import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptInviteEmailComponent} from './script-invite-email.component';

describe('ScriptInviteEmailComponent', () => {
  let component: ScriptInviteEmailComponent;
  let fixture: ComponentFixture<ScriptInviteEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptInviteEmailComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptInviteEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
