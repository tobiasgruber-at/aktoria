import {FormGroup} from '@angular/forms';
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
      this.processSubmit();
    } else {
      console.log('Invalid input');
    }
  }

  /** Toggles the loading state. */
  toggleLoading(loading: boolean = !this.loading): void {
    this.loading = loading;
    if (this.loading) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  /** Handles common errors by notifying the user. */
  handleError(error): void {
    this.toggleLoading(false);
    this.ts.showError(error);
    console.log(error);
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
      this.submitted &&
      field.invalid &&
      (errorName ? field.errors[errorName] : true)
    );
  }

  /** Sends the submitted value, after the form is validated. */
  protected abstract processSubmit(): void;
}
