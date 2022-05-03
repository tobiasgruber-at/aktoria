import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';
import { appearAnimations } from '../../animations/appear-animations';

/** @author Tobias Gruber */
@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [appearAnimations]
})
export class AlertComponent {
  @Output() closeAlert = new EventEmitter<void>();
  @Input() show = false;
  @Input() type: 'danger' | 'success' | 'info' = 'success';
  @Input() closeable = true;

  close(): void {
    this.closeAlert.emit();
  }
}
