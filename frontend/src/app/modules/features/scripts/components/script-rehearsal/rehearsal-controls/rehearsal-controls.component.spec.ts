import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RehearsalControlsComponent} from './rehearsal-controls.component';

describe('RehearsalControlsComponent', () => {
  let component: RehearsalControlsComponent;
  let fixture: ComponentFixture<RehearsalControlsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RehearsalControlsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RehearsalControlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
