import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Line, Role } from '../../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../../services/script-viewer.service';
import { FormBase } from '../../../../../../shared/classes/form-base';
import { FormBuilder, Validators } from '@angular/forms';
import { ToastService } from '../../../../../../core/services/toast/toast.service';

@Component({
  selector: 'app-change-line-modal',
  templateUrl: './change-line-modal.component.html',
  styleUrls: ['./change-line-modal.component.scss']
})
export class ChangeLineModalComponent extends FormBase implements OnInit {
  @Input() modal: NgbActiveModal;
  @Input() line: Line;

  constructor(
    public scriptViewerService: ScriptViewerService,
    private formBuilder: FormBuilder,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      roles: [[...this.line?.roles]],
      content: [
        this.line.content,
        [Validators.required, Validators.maxLength(1000)]
      ]
    });
  }

  changeRoles(toggledRole: Role): void {
    this.form.get('roles').markAsTouched();
    const selectedRolesPrev = [...this.form.value.roles];
    let selectedRolesUpdated: Role[];
    if (selectedRolesPrev.some((r) => r.name === toggledRole.name)) {
      selectedRolesUpdated = selectedRolesPrev.filter(
        (r) => r.name !== toggledRole.name
      );
    } else {
      selectedRolesUpdated = [...selectedRolesPrev, toggledRole];
    }
    this.form.patchValue({
      roles: selectedRolesUpdated
    });
  }

  protected processSubmit(): void {
    const { roles, content } = this.form.value;
    this.line.roles = roles;
    this.line.content = content;
    this.modal.dismiss();
  }
}
