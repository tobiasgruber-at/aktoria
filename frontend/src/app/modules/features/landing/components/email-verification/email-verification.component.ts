import { Component, OnInit } from '@angular/core';
import { FormBase } from '../../../../shared/classes/form-base';
import { ToastService } from '../../../../core/services/toast.service';
import { FormBuilder } from '@angular/forms';
import { UserService } from '../../../../core/services/user/user-service';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.scss']
})
export class EmailVerificationComponent extends FormBase implements OnInit {
  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({});
  }

  protected sendSubmit(): void {
    this.userService.resendVerificationEmail().subscribe(
      () => {
        this.toggleLoading(false);
      },
      (err) => this.handleError(err)
    );
  }
}
