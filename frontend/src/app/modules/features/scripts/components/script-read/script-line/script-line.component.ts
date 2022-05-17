import { Component, HostBinding, Input } from '@angular/core';
import { Line } from '../../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-line',
  templateUrl: './script-line.component.html',
  styleUrls: ['./script-line.component.scss']
})
export class ScriptLineComponent {
  @Input() line: Line;

  constructor() {}

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isInstruction) {
      classes.push('py-2');
    } else {
    }
    return classes;
  }

  /** @return Whether this line is an instruction, or a spoken line. */
  get isInstruction() {
    return this.line?.roles?.length < 1;
  }
}
