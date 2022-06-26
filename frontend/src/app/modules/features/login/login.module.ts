import {NgModule} from '@angular/core';
import {SharedModule} from '../../shared/shared.module';
import {LoginComponent} from './login.component';
import {LoginRoutingModule} from './login-routing.module';

/** Login module that consists of all login related parts. */
@NgModule({
  declarations: [LoginComponent],
  imports: [SharedModule, LoginRoutingModule]
})
export class LoginModule {
}
