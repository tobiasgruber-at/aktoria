import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ScriptsComponent} from './scripts.component';
import {ScriptUploadComponent} from './components/script-upload/script-upload.component';
import {ScriptOverviewComponent} from './components/script-overview/script-overview.component';
import {ScriptReadComponent} from './components/script-read/script-read.component';

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
  },
  {
    path: ':id/read',
    component: ScriptReadComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ScriptsRoutingModule {
}
