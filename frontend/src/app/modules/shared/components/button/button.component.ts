import {Component, Input, OnInit} from '@angular/core';
import {Theme} from '../../enums/theme.enum';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnInit {
  @Input() loading = false;
  @Input() disabled = false;
  @Input() label: string;
  @Input() theme: Theme = Theme.primary;
  @Input() type: 'button' | 'submit' = 'button';

  constructor() {
  }

  ngOnInit(): void {
  }
}
