import { Component, HostBinding, OnInit } from '@angular/core';
import { appearAnimations } from '../../../animations/appear-animations';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.scss'],
  animations: [appearAnimations]
})
export class FormErrorComponent implements OnInit {
  constructor() {}

  @HostBinding('@appear')
  get appearAnimation(): boolean {
    return true;
  }

  ngOnInit(): void {}
}
