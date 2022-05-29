import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ScriptViewerService} from '../../../services/script-viewer.service';
import {Line, Role, SimpleScript} from '../../../../../shared/dtos/script-dtos';
import {Subject, takeUntil} from 'rxjs';
import {FormBase} from '../../../../../shared/classes/form-base';
import {ToastService} from '../../../../../core/services/toast/toast.service';
import {FormBuilder, Validators} from '@angular/forms';
import {arrayMinLengthValidator} from '../../../../../shared/validators/array-min-length';
import {Theme} from '../../../../../shared/enums/theme.enum';
import {RoleService} from '../../../../../core/services/role/role.service';

@Component({
  selector: 'app-merge-roles-modal',
  templateUrl: './merge-roles-modal.component.html',
  styleUrls: ['./merge-roles-modal.component.scss']
})
export class MergeRolesModalComponent
  extends FormBase
  implements OnInit, OnDestroy {
  @Input() modal: NgbActiveModal;
  script: SimpleScript = null;
  private isUploading = false;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private formBuilder: FormBuilder,
    private toastService: ToastService,
    private roleService: RoleService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      roleName: ['', [Validators.required, Validators.maxLength(100)]],
      selectedRoles: [[], [arrayMinLengthValidator(2)]]
    });
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
    this.scriptViewerService.$isUploading
      .pipe(takeUntil(this.$destroy))
      .subscribe((isUploading) => {
        this.isUploading = isUploading;
      });
  }

  updateRoles(toggledRole: Role): void {
    this.form.get('selectedRoles').markAsTouched();
    const selectedRolesPrev = [...this.form.value.selectedRoles];
    let selectedRolesUpdated: Role[];
    if (selectedRolesPrev.some((r) => r.name === toggledRole.name)) {
      selectedRolesUpdated = selectedRolesPrev.filter(
        (r) => r.name !== toggledRole.name
      );
    } else {
      selectedRolesUpdated = [...selectedRolesPrev, toggledRole];
    }
    this.form.patchValue({
      selectedRoles: selectedRolesUpdated
    });
  }

  ngOnDestroy() {
  }

  /*checkDuplicatedRole(fieldName: string): boolean {
    const field = this.form.get(fieldName);
    return this.script.roles.some((r) => r.name === field.value);
  }*/

  protected processSubmit(): void {
    let {roleName} = this.form.value;
    const {selectedRoles} = this.form.value;
    roleName = roleName.toUpperCase();
    if (this.isUploading) {
      this.updateStateAfterMerge(selectedRoles, {
        id: null,
        name: roleName.toUpperCase(),
        color: null
      });
    } else {
      this.roleService
        .mergeRoles(
          {
            ids: selectedRoles.map((r) => r.id),
            newName: roleName
          },
          this.script.getId()
        )
        .subscribe({
          next: (mergedRole) => {
            this.updateStateAfterMerge(selectedRoles, mergedRole);
            this.modal.dismiss();
          },
          error: (err) => {
            this.handleError(err);
          }
        });
    }
  }

  /** Updates the state after a merge */
  private updateStateAfterMerge(selectedRoles: Role[], mergedRole: Role): void {
    const replaceRoles = (obj: SimpleScript | Line) => {
      if (obj === null || obj.roles === null) {
        return;
      }
      const prevRolesLength = obj.roles.length;
      selectedRoles.forEach((removedRole) => {
        obj.roles = obj.roles.filter((r) => r.name !== removedRole.name);
      });
      if (obj.roles.length < prevRolesLength) {
        obj.roles.push(mergedRole);
      }
    };
    replaceRoles(this.script);
    this.script.pages.forEach((page) => {
      page.lines.forEach((line) => {
        replaceRoles(line);
      });
    });
    this.modal.dismiss();
    this.toastService.show({
      message: 'Rollen erfolgreich vereint.',
      theme: Theme.primary
    });
  }
}
