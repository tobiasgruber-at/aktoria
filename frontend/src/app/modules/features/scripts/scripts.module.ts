import {NgModule} from '@angular/core';
import {ScriptsComponent} from './scripts.component';
import {SharedModule} from '../../shared/shared.module';
import {ScriptsRoutingModule} from './scripts-routing.module';
import {ScriptListComponent} from './components/script-list/script-list.component';
import {ScriptListItemComponent} from './components/script-list/script-list-item/script-list-item.component';
import {ScriptUploadComponent} from './components/script-upload/script-upload.component';
import {UploadInformationComponent} from './components/script-upload/upload-information/upload-information.component';
import {ScriptOverviewComponent} from './components/script-overview/script-overview.component';
import {ScriptReadComponent} from './components/script-read/script-read.component';
import {ScriptLineComponent} from './components/script-viewer/script-line/script-line.component';
import {ScriptPageComponent} from './components/script-viewer/script-page/script-page.component';
import {ScriptViewerComponent} from './components/script-viewer/script-viewer.component';
import {ScriptRolesListComponent} from './components/script-roles-list/script-roles-list.component';
import {ScriptInviteComponent} from './components/script-invite/script-invite.component';
import {ScriptInviteAcceptComponent} from './components/script-invite-accept/script-invite-accept.component';
import {
  ScriptMembersListComponent
} from './components/script-overview/script-members-list/script-members-list.component';
import {
  ScriptMembersItemComponent
} from './components/script-overview/script-members-list/script-members-item/script-members-item.component';
import {ScriptInviteLinkComponent} from './components/script-invite/script-invite-link/script-invite-link.component';
import {ScriptInviteEmailComponent} from './components/script-invite/script-invite-email/script-invite-email.component';
import {ScriptEditorComponent} from './components/script-editor/script-editor.component';
import {MergeRolesModalComponent} from './components/script-editor/merge-roles-modal/merge-roles-modal.component';
import {
  ChangeLineModalComponent
} from './components/script-viewer/script-line/change-line-modal/change-line-modal.component';
import {ScriptRehearsalComponent} from './components/script-rehearsal/script-rehearsal.component';
import {RehearsalSectionComponent} from './components/rehearsal-section/rehearsal-section.component';
import {RehearsalLineComponent} from './components/script-rehearsal/rehearsal-line/rehearsal-line.component';
import {
  RehearsalControlsComponent
} from './components/script-rehearsal/rehearsal-controls/rehearsal-controls.component';
import {
  RehearsalSectionCreateComponent
} from './components/script-rehearsal/script-rehearsal-sections/rehearsal-section-create/rehearsal-section-create.component';
import {
  ScriptRehearsalSectionsComponent
} from './components/script-rehearsal/script-rehearsal-sections/script-rehearsal-sections.component';
import {ScriptRehearsalService} from './services/script-rehearsal.service';
import {
  ScriptReadControlsComponent
} from './components/script-read/script-read-controls/script-read-controls.component';
import {
  SelectLineTextComponent
} from './components/script-rehearsal/script-rehearsal-sections/rehearsal-section-create/select-line-text/select-line-text.component';
import {VoiceRecordingService} from './services/voice-recording.service';
import {VoiceSpeakingService} from './services/voice-speaking.service';
import {ScriptReviewComponent} from './components/script-review/script-review.component';
import {ScriptConflictsComponent} from './components/script-conflicts/script-conflicts.component';

/** Scripts module that consists of all script related parts (script viewer, rehearsal, dashboard, scripts list, ..). */
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
    ScriptRolesListComponent,
    ScriptInviteComponent,
    ScriptInviteAcceptComponent,
    ScriptMembersListComponent,
    ScriptMembersItemComponent,
    ScriptInviteLinkComponent,
    ScriptInviteEmailComponent,
    ScriptEditorComponent,
    ScriptRolesListComponent,
    MergeRolesModalComponent,
    ChangeLineModalComponent,
    ScriptRehearsalComponent,
    ScriptRehearsalSectionsComponent,
    RehearsalSectionComponent,
    RehearsalLineComponent,
    RehearsalControlsComponent,
    RehearsalSectionCreateComponent,
    ScriptReadControlsComponent,
    SelectLineTextComponent,
    ScriptReviewComponent,
    ScriptConflictsComponent
  ],
  providers: [
    ScriptRehearsalService,
    VoiceRecordingService,
    VoiceSpeakingService
  ],
  imports: [SharedModule, ScriptsRoutingModule]
})
export class ScriptsModule {
}
