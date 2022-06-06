import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {UserService} from '../../../../core/services/user/user.service';
import {Router} from '@angular/router';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {FormBase} from '../../../../shared/classes/form-base';
import {matchingPasswordsValidator} from '../../../../shared/validators/matching-passwords-validator';
import {Theme} from '../../../../shared/enums/theme.enum';
import {SimpleUser, UpdateUser} from '../../../../shared/dtos/user-dtos';
import {appearAnimations} from '../../../../shared/animations/appear-animations';
import {Subject, takeUntil} from 'rxjs';

/** @author Simon Josef Kreuzpointner */
@Component({
  selector: 'app-profile-change',
  templateUrl: './profile-change.component.html',
  styleUrls: ['./profile-change.component.scss'],
  animations: [appearAnimations]
})
export class ProfileChangeComponent
  extends FormBase
  implements OnInit, OnDestroy {
  user: SimpleUser;
  showPasswordChange = false;
  private $destroy = new Subject<void>();
  /**
   * Cached password-change form-data.
   *
   * @description In case the user toggles the show-password section,
   * this part of the form should either be cleared or refilled.
   */
  private cachedPasswordChangeData: {
    oldPassword: string;
    password: string;
    passwordConfirm: string;
  } = {
    oldPassword: null,
    password: null,
    passwordConfirm: null
  };

  constructor(
    private formBuilder: FormBuilder,
    public userService: UserService,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group(
      {
        firstName: [null, [Validators.maxLength(100)]],
        lastName: [null, [Validators.maxLength(100)]],
        email: [null, [Validators.maxLength(100), Validators.email]],
        oldPassword: [null, [Validators.minLength(8)]],
        password: [null, [Validators.minLength(8)]],
        passwordConfirm: [null]
      },
      { validators: [matchingPasswordsValidator(true)] }
    );
    this.userService.$ownUser
      .pipe(takeUntil(this.$destroy))
      .subscribe((user) => {
        this.user = user;
        if (this.user) {
          this.initForm();
        }
      });
  }

  /**
   * Toggles whether the change-password part of the form is shown.
   *
   * @description If it should collapse, the form fields are cleared and cached,
   * so that validation is ignored until it's shown again, where the cached data
   * will be filled out again.
   */
  toggleShowPasswordChange(): void {
    this.showPasswordChange = !this.showPasswordChange;
    if (!this.showPasswordChange) {
      const { oldPassword, password, passwordConfirm } = this.form.value;
      this.cachedPasswordChangeData = {
        oldPassword,
        password,
        passwordConfirm
      };
      this.form.patchValue({
        oldPassword: null,
        password: null,
        passwordConfirm: null
      });
    } else {
      this.form.patchValue(this.cachedPasswordChangeData);
    }
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  /** Submits only the changed data. */
  protected processSubmit() {
    let { firstName, lastName, email } = this.form.value;
    const { oldPassword, password } = this.form.value;
    if (firstName === this.user.firstName) {
      firstName = null;
    }
    if (lastName === this.user.lastName) {
      lastName = null;
    }
    if (email === this.user.email) {
      email = null;
    }
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
          this.userService.setOwnUser(res);
          this.toastService.show({
            message: 'Erfolgreich geändert!',
            theme: Theme.primary
          });
          this.router.navigateByUrl('/profile').then();
        },
        error: (err) => this.handleError(err)
      });
  }

  private initForm(): void {
    this.form.patchValue({
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email
    });
  }
}
