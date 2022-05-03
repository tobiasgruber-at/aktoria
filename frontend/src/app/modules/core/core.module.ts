import { NgModule } from '@angular/core';
import { httpInterceptorProviders } from './interceptors';
import { SharedModule } from '../shared/shared.module';
import { ToastsComponent } from './components/toasts/toasts.component';
import { ToastsItemComponent } from './components/toasts/toasts-item/toasts-item.component';

const sharedDeclarations = [ToastsComponent];

@NgModule({
  declarations: [...sharedDeclarations, ToastsItemComponent],
  imports: [SharedModule],
  exports: [...sharedDeclarations],
  providers: [httpInterceptorProviders]
})
export class CoreModule {}
