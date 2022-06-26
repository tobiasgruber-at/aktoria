import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RestorePasswordComponent} from './restore-password.component';

/** Restore password module that consists of all related parts. */
const routes: Routes = [
  {
    path: '',
    component: RestorePasswordComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RestorePasswordRoutingModule {
}
