import { Component, Input } from '@angular/core';
import { Page } from '../../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-page',
  templateUrl: './script-page.component.html',
  styleUrls: ['./script-page.component.scss']
})
export class ScriptPageComponent {
  @Input() page: Page;
}
