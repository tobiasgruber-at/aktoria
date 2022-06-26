import {Component} from '@angular/core';
import {ScriptViewerService} from '../../services/script-viewer.service';

/** Script viewer, that renders the script. */
@Component({
  selector: 'app-script-viewer',
  templateUrl: './script-viewer.component.html',
  styleUrls: ['./script-viewer.component.scss']
})
export class ScriptViewerComponent {
  constructor(public scriptViewerService: ScriptViewerService) {
  }
}
