import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeLineModalComponent } from './change-line-modal.component';

describe('ChangeLineRolesModalComponent', () => {
  let component: ChangeLineModalComponent;
  let fixture: ComponentFixture<ChangeLineModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChangeLineModalComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeLineModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
