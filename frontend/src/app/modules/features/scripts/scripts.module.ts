import { NgModule } from '@angular/core';
import { ScriptsComponent } from './scripts.component';
import { SharedModule } from '../../shared/shared.module';
import { ScriptsRoutingModule } from './scripts-routing.module';

@NgModule({
  declarations: [ScriptsComponent],
  imports: [SharedModule, ScriptsRoutingModule]
})
export class ScriptsModule {}
