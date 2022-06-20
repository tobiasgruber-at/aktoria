import {NgModule} from '@angular/core';
import {LandingComponent} from './landing.component';
import {SharedModule} from '../../shared/shared.module';
import {LandingRoutingModule} from './landing-routing.module';
import {EmailVerificationComponent} from './components/email-verification/email-verification.component';

/** Landing module that consists of all related parts (landing page, email verification, ..). */
@NgModule({
  declarations: [LandingComponent, EmailVerificationComponent],
  imports: [SharedModule, LandingRoutingModule]
})
export class LandingModule {
}
