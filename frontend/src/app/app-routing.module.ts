import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './modules/features/landing/landing.component';
import { AuthGuard } from './modules/core/guards/auth.guard';

const routes: Routes = [
  { path: '', component: LandingComponent },
  {
    path: 'login',
    loadChildren: () =>
      import('./modules/features/login/login.module').then((m) => m.LoginModule)
  },
  {
    path: 'register',
    loadChildren: () =>
      import('./modules/features/registration/registration.module').then(
        (m) => m.RegistrationModule
      )
  },
  {
    path: 'landing',
    loadChildren: () =>
      import('./modules/features/landing/landing.module').then(
        (m) => m.LandingModule
      )
  },
  {
    path: 'scripts',
    //TODO: uncomment
    //canActivate: [AuthGuard],
    loadChildren: () =>
      import('./modules/features/scripts/scripts.module').then(
        (m) => m.ScriptsModule
      )
  },
  {
    path: 'verifyEmail/:token',
    loadChildren: () =>
      import('./modules/features/verify-email/verify-email.module').then(
        (m) => m.VerifyEmailModule
      )
  },
  {
    path: 'password/reset',
    loadChildren: () =>
      import('./modules/features/password/reset-password/reset-password.module').then(
        (m) => m.ResetPasswordModule
      )
  },
  {
    path: 'password/restore/:token',
    loadChildren: () =>
      import('./modules/features/password/restore-password/restore-password.module').then(
        (m) => m.RestorePasswordModule
      )
  },
  // TODO: remove sometime
  {
    path: 'message',
    canActivate: [AuthGuard],
    loadChildren: () =>
      import('./modules/features/message/message.module').then(
        (m) => m.MessageModule
      )
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
