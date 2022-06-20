import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProfileComponent} from './profile.component';
import {ProfileChangeComponent} from './components/profile-change/profile-change.component';

/** Profile module that consists of all related parts. */
const routes: Routes = [
  {
    path: '',
    component: ProfileComponent
  },
  {
    path: 'change',
    component: ProfileChangeComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule {
}
