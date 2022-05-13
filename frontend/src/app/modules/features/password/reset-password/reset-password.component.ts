import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ToastService} from '../../../core/services/toast/toast.service';
import {FormBase} from '../../../shared/classes/form-base';
import {UserService} from '../../../core/services/user/user-service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent extends FormBase implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router,
    toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  protected sendSubmit(): void {
    const { email } = this.form.value;
    console.log('Try to reset password of user: ' + email);
    this.userService.forgotPassword(email).subscribe({
      next: () => {
        this.toggleLoading(false);
        console.log('Successfully send token to: ' + email);
      },
      error: (err) => this.handleError(err)
    });
  }

}
