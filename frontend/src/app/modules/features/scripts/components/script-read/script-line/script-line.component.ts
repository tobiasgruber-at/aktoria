import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {Line} from '../../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-line',
  templateUrl: './script-line.component.html',
  styleUrls: ['./script-line.component.scss']
})
export class ScriptLineComponent implements OnInit {
  @Input() line: Line;

  constructor() {
  }

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isInstruction) {
      classes.push('py-2');
    } else {
    }
    return classes;
  }

  get isInstruction() {
    return this.line?.roles?.length < 1;
  }

  ngOnInit(): void {
  }
}
