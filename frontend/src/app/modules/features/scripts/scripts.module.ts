import {NgModule} from '@angular/core';
import {ScriptsComponent} from './scripts.component';
import {SharedModule} from '../../shared/shared.module';
import {ScriptsRoutingModule} from './scripts-routing.module';
import {ScriptListComponent} from './components/script-list/script-list.component';
import {ScriptListItemComponent} from './components/script-list-item/script-list-item.component';
import {ScriptUploadComponent} from './components/script-upload/script-upload.component';
import {NgbCollapseModule} from '@ng-bootstrap/ng-bootstrap';
import {UploadInformationComponent} from './components/upload-information/upload-information.component';
import {ScriptOverviewComponent} from './components/script-overview/script-overview.component';
import {ScriptReadComponent} from './components/script-read/script-read.component';
import {ScriptPageComponent} from './components/script-read/script-page/script-page.component';
import {ScriptLineComponent} from './components/script-read/script-line/script-line.component';

@NgModule({
  declarations: [
    ScriptsComponent,
    ScriptListComponent,
    ScriptListItemComponent,
    ScriptUploadComponent,
    UploadInformationComponent,
    ScriptOverviewComponent,
    ScriptReadComponent,
    ScriptPageComponent,
    ScriptLineComponent
  ],
  imports: [SharedModule, ScriptsRoutingModule, NgbCollapseModule]
})
export class ScriptsModule {
}
