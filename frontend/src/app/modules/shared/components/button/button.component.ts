import {Component, Input} from '@angular/core';
import {Theme} from '../../enums/theme.enum';

/** Common button. */
@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent {
  @Input() loading = false;
  @Input() disabled = false;
  @Input() label: string;
  @Input() theme: Theme = Theme.primary;
  @Input() type: 'button' | 'submit' = 'button';
}
