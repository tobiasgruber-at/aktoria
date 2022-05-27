import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Role } from '../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-roles-list',
  templateUrl: './script-roles-list.component.html',
  styleUrls: ['./script-roles-list.component.scss']
})
export class ScriptRolesListComponent {
  @Input() roles: Role[] = [];
  /** The selected role. Alternative to selectedRoles. */
  @Input() selectedRole: Role = null;
  /** The selected roles. Alternative to selectedRole. */
  @Input() selectedRoles: Role[] = [];
  @Input() size: 'lg' | 'xl' = 'lg';
  @Input() theme: 'primary' | 'secondary' = 'secondary';
  @Output() roleClicked = new EventEmitter<Role>();

  onRoleClicked(role: Role): void {
    this.roleClicked.emit(role);
  }

  isRoleSelected(role: Role): boolean {
    const compareRoles = (r1, r2) => r1.name === r2.name;
    return this.selectedRole
      ? compareRoles(this.selectedRole, role)
      : this.selectedRoles?.some((r) => compareRoles(r, role));
  }
}
