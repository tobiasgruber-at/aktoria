import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoggedInGuard } from './modules/core/guards/logged-in-guard.service';
import { LoggedOutGuard } from './modules/core/guards/logged-out-guard.service';
import {VerifiedGuard} from './modules/core/guards/verified.guard';
import {NotVerifiedGuard} from './modules/core/guards/not-verified.guard';

const routes: Routes = [
  {
    path: '',
    canActivate: [NotVerifiedGuard],
    loadChildren: () =>
      import('./modules/features/landing/landing.module').then(
        (m) => m.LandingModule
      )
  },
  {
    path: 'login',
    canActivate: [LoggedOutGuard],
    loadChildren: () =>
      import('./modules/features/login/login.module').then((m) => m.LoginModule)
  },
  {
    path: 'register',
    canActivate: [LoggedOutGuard],
    loadChildren: () =>
      import('./modules/features/registration/registration.module').then(
        (m) => m.RegistrationModule
      )
  },
  {
    path: 'scripts',
    canActivate: [VerifiedGuard],
    loadChildren: () =>
      import('./modules/features/scripts/scripts.module').then(
        (m) => m.ScriptsModule
      )
  },
  {
    path: 'profile',
    canActivate: [LoggedInGuard],
    loadChildren: () =>
      import('./modules/features/profile/profile.module').then(
        (m) => m.ProfileModule
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
      import(
        './modules/features/password/reset-password/reset-password.module'
      ).then((m) => m.ResetPasswordModule)
  },
  {
    path: 'password/restore/:token',
    loadChildren: () =>
      import(
        './modules/features/password/restore-password/restore-password.module'
      ).then((m) => m.RestorePasswordModule)
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
