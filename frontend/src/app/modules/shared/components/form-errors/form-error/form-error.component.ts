import {Component, HostBinding} from '@angular/core';
import {appearAnimations} from '../../../animations/appear-animations';

/** Common form error. */
@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html',
  styleUrls: ['./form-error.component.scss'],
  animations: [appearAnimations]
})
export class FormErrorComponent {
  @HostBinding('@appear')
  get appearAnimation(): boolean {
    return true;
  }
}
