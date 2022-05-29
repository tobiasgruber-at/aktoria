import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RehearsalSectionComponent } from './rehearsal-section.component';

describe('RehearsalSectionComponent', () => {
  let component: RehearsalSectionComponent;
  let fixture: ComponentFixture<RehearsalSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RehearsalSectionComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RehearsalSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
