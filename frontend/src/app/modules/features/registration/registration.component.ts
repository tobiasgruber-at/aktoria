import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ToastService} from '../../core/services/toast/toast.service';
import {UserService} from '../../core/services/user/user-service';
import {UserRegistration} from '../../shared/dtos/user-dtos';
import {FormBase} from '../../shared/classes/form-base';
import {Theme} from '../../shared/enums/theme.enum';
import {matchingPasswordsValidator} from '../../shared/validators/matching-passwords-validator';

/** @author Tobias Gruber */
@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent extends FormBase implements OnInit {
  constructor(
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
        firstName: ['', [Validators.required, Validators.maxLength(100)]],
        lastName: ['', [Validators.required, Validators.maxLength(100)]],
        email: [
          '',
          [Validators.required, Validators.maxLength(100), Validators.email]
        ],
        password: ['', [Validators.required, Validators.minLength(8)]],
        passwordConfirm: ['', [Validators.required]]
      },
      { validators: [matchingPasswordsValidator] }
    );
  }

  protected sendSubmit() {
    const { firstName, lastName, email, password } = this.form.value;
    this.userService
      .register(new UserRegistration(firstName, lastName, email, password))
      .subscribe({
        next: (res) => {
          this.toggleLoading(false);
          this.router.navigateByUrl('/');
          this.toastService.show({
            message: 'Erfolgreich registriert!',
            theme: Theme.primary
          });
        },
        error: (err) => this.handleError(err)
      });
  }
}
