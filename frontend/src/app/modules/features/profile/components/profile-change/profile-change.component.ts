import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../../../core/services/user/user-service';
import { Router } from '@angular/router';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { AuthService } from '../../../../core/services/auth/auth-service';
import { FormBase } from '../../../../shared/classes/form-base';
import { matchingPasswordsValidator } from '../../../../shared/validators/matching-passwords-validator';
import { Theme } from '../../../../shared/enums/theme.enum';
import { SimpleUser, UpdateUser } from '../../../../shared/dtos/user-dtos';
import { appearAnimations } from '../../../../shared/animations/appear-animations';

/** @author Simon Josef Kreuzpointner */
@Component({
  selector: 'app-profile-change',
  templateUrl: './profile-change.component.html',
  styleUrls: ['./profile-change.component.scss'],
  animations: [appearAnimations]
})
export class ProfileChangeComponent extends FormBase implements OnInit {
  user: SimpleUser;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router,
    private toastService: ToastService,
    private authService: AuthService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.getUser();

    this.form = this.formBuilder.group(
      {
        firstName: [null, [Validators.maxLength(100)]],
        lastName: [null, [Validators.maxLength(100)]],
        email: [null, [Validators.maxLength(100), Validators.email]],
        oldPassword: [null, [Validators.minLength(8)]],
        password: [null, [Validators.minLength(8)]],
        passwordConfirm: [null, []]
      },
      { validators: [matchingPasswordsValidator] }
    );
  }

  getUser() {
    this.authService.$loginChanges().subscribe((loggedIn) => {
      if (loggedIn) {
        this.user = this.userService.getOwnUser();
      }
    });
  }

  protected sendSubmit() {
    const { firstName, lastName, email, oldPassword, password } =
      this.form.value;
    this.userService
      .update(
        new UpdateUser(
          this.user.id,
          firstName,
          lastName,
          email,
          oldPassword,
          password,
          null
        )
      )
      .subscribe({
        next: (res) => {
          this.toastService.show({
            message: 'Erfolgreich geändert!',
            theme: Theme.primary
          });
          this.router.navigateByUrl('/profile').then();
        },
        error: (err) => this.handleError(err)
      });
  }
}
