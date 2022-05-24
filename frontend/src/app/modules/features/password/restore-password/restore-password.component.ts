import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../core/services/toast/toast.service';
import { FormBase } from '../../../shared/classes/form-base';
import { matchingPasswordsValidator } from '../../../shared/validators/matching-passwords-validator';
import { UserService } from '../../../core/services/user/user-service';
import { ChangePassword } from '../../../shared/dtos/password-change-dto';
import { Theme } from '../../../shared/enums/theme.enum';

@Component({
  selector: 'app-restore-password',
  templateUrl: './restore-password.component.html',
  styleUrls: ['./restore-password.component.scss']
})
export class RestorePasswordComponent extends FormBase implements OnInit {
  token: string;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        password: ['', [Validators.required, Validators.minLength(8)]],
        passwordConfirm: ['', [Validators.required]]
      },
      { validators: [matchingPasswordsValidator()] }
    );
    this.token = this.route.snapshot.paramMap.get('token');
  }

  protected processSubmit(): void {
    const { password } = this.form.value;
    this.userService
      .changePassword(new ChangePassword(this.token, null, password))
      .subscribe({
        next: () => {
          this.toggleLoading(false);
          this.toastService.show({
            message: 'Passwort wurde geändert!',
            theme: Theme.primary
          });
        },
        error: (err) => this.handleError(err)
      });
  }
}
