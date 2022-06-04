import { NgModule } from '@angular/core';
import { httpInterceptorProviders } from './interceptors';
import { SharedModule } from '../shared/shared.module';
import { ToastsComponent } from './components/toasts/toasts.component';
import { ToastsItemComponent } from './components/toasts/toasts-item/toasts-item.component';
import { UserService } from './services/user/user-service';
import { UserImplService } from './services/user/user-impl.service';

const sharedDeclarations = [ToastsComponent];

/**
 * Core module that consists of global and static services and components.
 *
 * @see https://reset.inso.tuwien.ac.at/repo/2022ss-sepm-pr-group/22ss-sepm-pr-qse-14/-/wikis/Frontend-Architektur
 */
@NgModule({
  declarations: [...sharedDeclarations, ToastsItemComponent],
  imports: [SharedModule],
  exports: [...sharedDeclarations],
  providers: [
    httpInterceptorProviders,
    {
      provide: UserService,
      useClass: UserImplService
    }
  ]
})
export class CoreModule {}
