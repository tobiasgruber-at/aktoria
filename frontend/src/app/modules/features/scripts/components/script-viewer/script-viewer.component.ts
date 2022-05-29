import {Component} from '@angular/core';
import {ScriptViewerService} from '../../services/script-viewer.service';

@Component({
  selector: 'app-script-viewer',
  templateUrl: './script-viewer.component.html',
  styleUrls: ['./script-viewer.component.scss']
})
export class ScriptViewerComponent {
  constructor(public scriptViewerService: ScriptViewerService) {
  }
}
