import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Theme} from '../../../shared/enums/theme.enum';
import {Toast} from '../../../shared/interfaces/toast';

/** @author Tobias Gruber */
@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toasts: Toast[] = [];
  private toastsSubject = new BehaviorSubject<Toast[]>([]);
  private readonly maxLength = 4;

  /** @return Observable of all toasts */
  get $toasts(): Observable<Toast[]> {
    return this.toastsSubject.asObservable();
  }

  /** Shows a toast. */
  show(toast: Toast): void {
    this.toasts.push(toast);
    if (this.toasts.length > this.maxLength) {
      this.toasts.shift();
    }
    this.toastsSubject.next(this.toasts);
  }

  showError(error): void {
    let errorMessage = '';
    if (error.error?.message) {
      errorMessage = error.error?.message;
    } else if (error.message) {
      errorMessage = error.message;
    } else if (error.error) {
      errorMessage = error.error;
    }
    this.show({
      message: errorMessage || 'Ein Fehler ist aufgetreten!',
      theme: Theme.danger
    });
  }

  /** Closes a toast. */
  close(toast): void {
    this.toasts = this.toasts.filter((t) => t !== toast);
    this.toastsSubject.next(this.toasts);
  }

  /** Closes all toasts. */
  clear(): void {
    this.toasts = [];
    this.toastsSubject.next(this.toasts);
  }
}
