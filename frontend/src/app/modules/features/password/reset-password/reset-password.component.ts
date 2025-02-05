import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ToastService} from '../../../core/services/toast/toast.service';
import {FormBase} from '../../../shared/classes/form-base';
import {UserService} from '../../../core/services/user/user.service';
import {Theme} from '../../../shared/enums/theme.enum';

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
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  protected processSubmit(): void {
    const {email} = this.form.value;
    console.log('Try to reset password of user: ' + email);
    this.userService.forgotPassword(email).subscribe({
      next: () => {
        this.toggleLoading(false);
        this.toastService.show({
          message: 'Passwort Wiederherstellungslink wurde gesendet!',
          theme: Theme.primary
        });
      },
      error: (err) => this.handleError(err)
    });
  }
}
