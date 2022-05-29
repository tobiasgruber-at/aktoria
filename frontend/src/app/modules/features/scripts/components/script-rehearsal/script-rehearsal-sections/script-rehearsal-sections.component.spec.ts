import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ScriptRehearsalSectionsComponent} from './script-rehearsal-sections.component';

describe('ScriptRehearsalSectionsComponent', () => {
  let component: ScriptRehearsalSectionsComponent;
  let fixture: ComponentFixture<ScriptRehearsalSectionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptRehearsalSectionsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptRehearsalSectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
