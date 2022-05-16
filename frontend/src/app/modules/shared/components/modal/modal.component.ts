import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Theme } from '../../enums/theme.enum';

@Component({
  selector: 'app-modal',
  templateUrl: 'modal.component.html',
  styleUrls: ['modal.component.scss']
})
export class ModalComponent {
  @Output() decline = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
  @Input() title: string;
  @Input() confirmBtnLabel: string;
  @Input() loading = false;
  readonly theme = Theme;

  onDecline(): void {
    if (this.loading) {
      return;
    }
    this.decline.emit();
  }

  onConfirm(): void {
    if (this.loading) {
      return;
    }
    this.confirm.emit();
  }
}
