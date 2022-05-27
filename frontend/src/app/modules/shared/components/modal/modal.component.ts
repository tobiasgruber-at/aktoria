import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Theme} from '../../enums/theme.enum';

/** Common modal. */
@Component({
  selector: 'app-modal',
  templateUrl: 'modal.component.html',
  styleUrls: ['modal.component.scss']
})
export class ModalComponent {
  @Output() decline = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
  @Input() title: string;
  @Input() theme: Theme = Theme.primary;
  @Input() confirmBtnLabel: string;
  @Input() loading = false;
  @Input() disabled = false;
  @Input() showDecline = true;

  /** If modal is declined. */
  onDecline(): void {
    if (this.loading) {
      return;
    }
    this.decline.emit();
  }

  /** If modal is confirmed. */
  onConfirm(): void {
    if (this.loading) {
      return;
    }
    this.confirm.emit();
  }
}
