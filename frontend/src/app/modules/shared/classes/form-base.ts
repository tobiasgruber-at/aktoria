import {FormGroup} from '@angular/forms';
import {Theme} from '../enums/theme.enum';
import {ToastService} from '../../core/services/toast/toast.service';

/** @author Tobias Gruber */
export abstract class FormBase {
  form: FormGroup;
  submitted = false;
  loading = false;

  protected constructor(private ts: ToastService) {}

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  submit(): void {
    this.submitted = true;
    if (this.form.valid) {
      this.toggleLoading(true);
      this.sendSubmit();
    } else {
      console.log('Invalid input');
    }
  }

  toggleLoading(loading: boolean = !this.loading): void {
    this.loading = loading;
    if (this.loading) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  handleError(error): void {
    this.toggleLoading(false);
    console.log('Could not log in due to:');
    console.log(error);
    let errorMessage = '';
    if (typeof error.error === 'object') {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error;
    }
    this.ts.show({
      message: errorMessage || 'Ein Fehler ist aufgetreten!',
      theme: Theme.danger
    });
  }

  /** @return Whether form has a specific error. */
  hasError(errorName?: string): boolean {
    return (
      this.submitted &&
      ((errorName ? this.form.errors?.[errorName] : this.form.invalid) || false)
    );
  }

  /** @return Whether a form field has any error, or a specific one. */
  fieldHasErrors(fieldName: string, errorName?: string): boolean {
    const field = this.form.get(fieldName);
    return (
      (this.submitted || field.touched) &&
      field.invalid &&
      (errorName ? field.errors[errorName] : true)
    );
  }

  protected abstract sendSubmit(): void;
}
