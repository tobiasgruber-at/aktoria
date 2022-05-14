import { NgModule } from '@angular/core';
import { ScriptsComponent } from './scripts.component';
import { SharedModule } from '../../shared/shared.module';
import { ScriptsRoutingModule } from './scripts-routing.module';
import { ScriptListComponent } from './components/script-list/script-list.component';
import { ScriptListItemComponent } from './components/script-list-item/script-list-item.component';
import { ScriptUploadComponent } from './components/script-upload/script-upload.component';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { UploadInformationComponent } from './components/upload-information/upload-information.component';

@NgModule({
  declarations: [
    ScriptsComponent,
    ScriptListComponent,
    ScriptListItemComponent,
    ScriptUploadComponent,
    UploadInformationComponent
  ],
  imports: [SharedModule, ScriptsRoutingModule, NgbCollapseModule]
})
export class ScriptsModule {}
