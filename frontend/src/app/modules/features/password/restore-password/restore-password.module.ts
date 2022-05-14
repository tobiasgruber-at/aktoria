import { NgModule } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { RestorePasswordRoutingModule } from './restore-password-routing.module';
import { RestorePasswordComponent } from './restore-password.component';

@NgModule({
  declarations: [RestorePasswordComponent],
  imports: [SharedModule, RestorePasswordRoutingModule]
})
export class RestorePasswordModule {}
