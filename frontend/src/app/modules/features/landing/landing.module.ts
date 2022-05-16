import {NgModule} from '@angular/core';
import {LandingComponent} from './landing.component';
import {SharedModule} from '../../shared/shared.module';
import {LandingRoutingModule} from './landing-routing.module';
import {EmailVerificationComponent} from './components/email-verification/email-verification.component';

@NgModule({
  declarations: [LandingComponent, EmailVerificationComponent],
  imports: [SharedModule, LandingRoutingModule]
})
export class LandingModule {
}
