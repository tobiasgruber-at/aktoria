import {Component, Input} from '@angular/core';
import {ScriptPreview} from '../../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-list-item',
  templateUrl: './script-list-item.component.html',
  styleUrls: ['./script-list-item.component.scss']
})
export class ScriptListItemComponent {
  @Input() script: ScriptPreview;
  @Input() isCreate = false;
}
