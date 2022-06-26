import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RehearsalSectionCreateComponent} from './rehearsal-section-create.component';

describe('RehearsalSectionCreateComponent', () => {
  let component: RehearsalSectionCreateComponent;
  let fixture: ComponentFixture<RehearsalSectionCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RehearsalSectionCreateComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RehearsalSectionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
