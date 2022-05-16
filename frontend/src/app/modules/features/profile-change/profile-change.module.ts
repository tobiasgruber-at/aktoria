import { NgModule } from '@angular/core';
import { ProfileChangeComponent } from './profile-change.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [ProfileChangeComponent],
  imports: [SharedModule]
})
export class ProfileChangeModule {}
