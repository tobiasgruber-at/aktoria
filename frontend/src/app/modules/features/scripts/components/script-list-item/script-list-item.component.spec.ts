import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptListItemComponent } from './script-list-item.component';

describe('ScriptComponent', () => {
  let component: ScriptListItemComponent;
  let fixture: ComponentFixture<ScriptListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScriptListItemComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
