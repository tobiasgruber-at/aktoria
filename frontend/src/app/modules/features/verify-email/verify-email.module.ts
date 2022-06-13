import {NgModule} from '@angular/core';
import {SharedModule} from '../../shared/shared.module';
import {VerifyEmailComponent} from './verify-email.component';
import {VerifyEmailRoutingModule} from './verify-email-routing.module';

/** Email verification module that consists of all related parts. */
@NgModule({
  declarations: [VerifyEmailComponent],
  imports: [SharedModule, VerifyEmailRoutingModule]
})
export class VerifyEmailModule {
}
