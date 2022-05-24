import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MergeRolesModalComponent } from './merge-roles-modal.component';

describe('MergeRolesModalComponent', () => {
  let component: MergeRolesModalComponent;
  let fixture: ComponentFixture<MergeRolesModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MergeRolesModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MergeRolesModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
