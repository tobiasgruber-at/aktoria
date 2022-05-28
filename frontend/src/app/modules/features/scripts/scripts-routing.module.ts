import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScriptsComponent } from './scripts.component';
import { ScriptUploadComponent } from './components/script-upload/script-upload.component';
import { ScriptOverviewComponent } from './components/script-overview/script-overview.component';
import { ScriptReadComponent } from './components/script-read/script-read.component';
import { ScriptInviteComponent } from './components/script-invite/script-invite.component';
import { ScriptInviteAcceptComponent } from './components/script-invite-accept/script-invite-accept.component';
import { ScriptEditComponent } from './components/script-edit/script-edit.component';
import { ScriptRehearsalSectionsComponent } from './components/script-rehearsal/script-rehearsal-sections/script-rehearsal-sections.component';

const routes: Routes = [
  {
    path: '',
    component: ScriptsComponent
  },
  {
    path: 'upload',
    component: ScriptUploadComponent
  },
  {
    path: 'upload/review',
    component: ScriptEditComponent,
    data: {
      isUploading: true
    }
  },
  {
    path: ':id',
    component: ScriptOverviewComponent
  },
  {
    path: ':id/edit',
    component: ScriptEditComponent,
    data: {
      isUploading: false
    }
  },
  {
    path: ':id/view',
    component: ScriptReadComponent
  },
  {
    path: ':id/rehearse/sections',
    component: ScriptRehearsalSectionsComponent
  },
  {
    path: ':id/invite',
    component: ScriptInviteComponent
  },
  {
    path: ':id/join/:token',
    component: ScriptInviteAcceptComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ScriptsRoutingModule {}
