import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SelectLineTextComponent} from './select-line-text.component';

describe('SelectLineTextComponent', () => {
  let component: SelectLineTextComponent;
  let fixture: ComponentFixture<SelectLineTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelectLineTextComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectLineTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
