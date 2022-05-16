import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptOverviewComponent} from './script-overview.component';

describe('ScriptOverviewComponent', () => {
  let component: ScriptOverviewComponent;
  let fixture: ComponentFixture<ScriptOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptOverviewComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
