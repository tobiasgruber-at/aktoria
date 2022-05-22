import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScriptsComponent } from './scripts.component';
import { ScriptUploadComponent } from './components/script-upload/script-upload.component';
import { ScriptOverviewComponent } from './components/script-overview/script-overview.component';
import { ScriptReadComponent } from './components/script-read/script-read.component';
import { ScriptUploadReviewComponent } from './components/script-upload/script-upload-review/script-upload-review.component';

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
    component: ScriptUploadReviewComponent
  },
  {
    path: ':id',
    component: ScriptOverviewComponent
  },
  {
    path: ':id/view',
    component: ScriptReadComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ScriptsRoutingModule {}
