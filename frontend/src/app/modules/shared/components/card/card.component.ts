import {Component, Input} from '@angular/core';

/** @author Tobias Gruber */
/** Common page-wrapping card. */
@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})
export class CardComponent {
  @Input() title: string;
}
