import { AbstractControl, ValidationErrors } from '@angular/forms';

export const matchingPasswordsValidator = (
  group: AbstractControl
): ValidationErrors | null => {
  const password = group.value.password;
  const passwordConfirm = group.value.passwordConfirm;
  return password === passwordConfirm ? null : { passwordsNotMatching: true };
};
