import { NgModule } from '@angular/core';
import { ScriptsComponent } from './scripts.component';
import { SharedModule } from '../../shared/shared.module';
import { ScriptsRoutingModule } from './scripts-routing.module';
import { ScriptListComponent } from './components/script-list/script-list.component';
import { ScriptListItemComponent } from './components/script-list/script-list-item/script-list-item.component';
import { ScriptUploadComponent } from './components/script-upload/script-upload.component';
import { UploadInformationComponent } from './components/script-upload/upload-information/upload-information.component';
import { ScriptOverviewComponent } from './components/script-overview/script-overview.component';
import { ScriptReadComponent } from './components/script-read/script-read.component';
import { ScriptLineComponent } from './components/script-viewer/script-line/script-line.component';
import { ScriptPageComponent } from './components/script-viewer/script-page/script-page.component';
import { ScriptViewerComponent } from './components/script-viewer/script-viewer.component';
import { ScriptRolesListComponent } from './components/script-roles-list/script-roles-list.component';
import { ScriptEditComponent } from './components/script-edit/script-edit.component';
import { MergeRolesModalComponent } from './components/script-edit/merge-roles-modal/merge-roles-modal.component';
import { ChangeLineModalComponent } from './components/script-viewer/script-line/change-line-modal/change-line-modal.component';

@NgModule({
  declarations: [
    ScriptsComponent,
    ScriptListComponent,
    ScriptListItemComponent,
    ScriptUploadComponent,
    UploadInformationComponent,
    ScriptOverviewComponent,
    ScriptViewerComponent,
    ScriptPageComponent,
    ScriptLineComponent,
    ScriptReadComponent,
    ScriptEditComponent,
    ScriptRolesListComponent,
    MergeRolesModalComponent,
    ChangeLineModalComponent
  ],
  imports: [SharedModule, ScriptsRoutingModule]
})
export class ScriptsModule {}
