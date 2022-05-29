import {AbstractControl, ValidationErrors} from '@angular/forms';

/**
 * Checks if passwords match.
 *
 * @description If the user is resetting a password, it also handles, that the old- and new passwords have
 * to be filled out and are not equal.
 */
export const matchingPasswordsValidator =
  (isResetting = false) =>
    (group: AbstractControl): ValidationErrors | null => {
      const password = group.value.password;
      const passwordConfirm = group.value.passwordConfirm;
      if (isResetting) {
        const oldPassword = group.value.oldPassword;
        if (oldPassword && !password) {
          return {requiredNewPassword: true};
        }
        if (!oldPassword && password) {
          return {requiredOldPassword: true};
        }
        if (oldPassword && password && oldPassword === password) {
          return {oldAndNewPasswordsEqual: true};
        }
      }
      return password === passwordConfirm ? null : {passwordsNotMatching: true};
    };
