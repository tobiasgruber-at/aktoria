import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScriptRolesListComponent } from './script-roles-list.component';

describe('ScriptRolesListComponent', () => {
  let component: ScriptRolesListComponent;
  let fixture: ComponentFixture<ScriptRolesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScriptRolesListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScriptRolesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
