import {Injectable} from '@angular/core';
import {Toast} from '../../../shared/interfaces/toast.interface';
import {BehaviorSubject, Observable} from 'rxjs';

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
