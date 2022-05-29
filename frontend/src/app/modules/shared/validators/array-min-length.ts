import {AbstractControl, ValidationErrors} from '@angular/forms';

/**
 * Checks if an array has a min-length.
 */
export const arrayMinLengthValidator =
  (minLength: number) =>
    (control: AbstractControl): ValidationErrors | null => {
      const array = control.value;
      return array?.length >= minLength ? null : {arrayMinLength: true};
    };
