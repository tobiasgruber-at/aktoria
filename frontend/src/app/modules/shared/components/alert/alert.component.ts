import {ChangeDetectionStrategy, Component, EventEmitter, HostBinding, Input, Output} from '@angular/core';
import {appearAnimations} from '../../animations/appear-animations';

/**
 * Common alert.
 *
 * @author Tobias Gruber
 */
@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [appearAnimations]
})
export class AlertComponent {
  @Output() closeAlert = new EventEmitter<void>();
  @Input() type: 'danger' | 'success' | 'info' = 'success';
  @Input() closeable = true;

  @HostBinding('class')
  get classes(): string {
    return 'd-flex w-100';
  }

  @HostBinding('@appear')
  get appearAnimation(): boolean {
    return true;
  }

  /** Closes the alert. */
  close(): void {
    this.closeAlert.emit();
  }
}
