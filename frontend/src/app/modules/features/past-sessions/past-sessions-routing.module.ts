import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PastSessionsComponent} from './past-sessions.component';

/** Past sessions module that consists of all related parts. */
const routes: Routes = [
  {
    path: '',
    component: PastSessionsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PastSessionsRoutingModule {
}
