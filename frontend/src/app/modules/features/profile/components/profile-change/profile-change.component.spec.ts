import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileChangeComponent } from './profile-change.component';
import { SharedTestingModule } from '../../../../shared/shared-testing.module';

describe('ProfileChangeComponent', () => {
  let component: ProfileChangeComponent;
  let fixture: ComponentFixture<ProfileChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedTestingModule],
      declarations: [ProfileChangeComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
