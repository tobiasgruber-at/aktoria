import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionListItemComponent } from './session-list-item.component';

describe('SessionListItemComponent', () => {
  let component: SessionListItemComponent;
  let fixture: ComponentFixture<SessionListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SessionListItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SessionListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
