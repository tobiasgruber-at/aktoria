import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastsItemComponent } from './toasts-item.component';

describe('ToastsItemComponent', () => {
  let component: ToastsItemComponent;
  let fixture: ComponentFixture<ToastsItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToastsItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ToastsItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
