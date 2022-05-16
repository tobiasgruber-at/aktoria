import { NgModule } from '@angular/core';
import { ProfileRoutingModule } from './profile-routing.module';
import { SharedModule } from '../../shared/shared.module';
import { ProfileComponent } from './profile.component';
import { ProfileChangeComponent } from './components/profile-change/profile-change.component';

@NgModule({
  declarations: [ProfileComponent, ProfileChangeComponent],
  imports: [SharedModule, ProfileRoutingModule]
})
export class ProfileModule {}
