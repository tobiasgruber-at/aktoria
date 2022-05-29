import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlsItemComponent } from './controls-item.component';

describe('ControlsItemComponent', () => {
  let component: ControlsItemComponent;
  let fixture: ComponentFixture<ControlsItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ControlsItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlsItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
