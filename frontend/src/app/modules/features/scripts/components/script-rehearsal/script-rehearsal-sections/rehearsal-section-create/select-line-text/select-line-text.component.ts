import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-select-line-text',
  templateUrl: './select-line-text.component.html',
  styleUrls: ['./select-line-text.component.scss']
})
export class SelectLineTextComponent {
  @Input() subject: 'Startzeile' | 'Endzeile';
  @Input() hasErrors = false;
  @Input() disabled = false;
}
