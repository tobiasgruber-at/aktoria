import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ScriptsComponent } from './scripts.component';
import { ScriptUploadComponent } from './components/script-upload/script-upload.component';
import { ScriptOverviewComponent } from './components/script-overview/script-overview.component';

const routes: Routes = [
  {
    path: '',
    component: ScriptsComponent
  },
  {
    path: 'new',
    component: ScriptUploadComponent
  },
  {
    path: ':id',
    component: ScriptOverviewComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ScriptsRoutingModule {}
