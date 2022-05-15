import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptLineComponent } from './script-line.component';

describe('ScriptLineComponent', () => {
  let component: ScriptLineComponent;
  let fixture: ComponentFixture<ScriptLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptLineComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
