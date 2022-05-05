import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptsComponent } from './scripts.component';
import { SharedTestingModule } from '../../shared/shared-testing.module';

describe('ScriptsComponent', () => {
  let component: ScriptsComponent;
  let fixture: ComponentFixture<ScriptsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptsComponent],
      imports: [SharedTestingModule]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
