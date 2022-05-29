import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RehearsalLineComponent} from './rehearsal-line.component';

describe('RehearsalLineComponent', () => {
  let component: RehearsalLineComponent;
  let fixture: ComponentFixture<RehearsalLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RehearsalLineComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RehearsalLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
