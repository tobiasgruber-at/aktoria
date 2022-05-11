import { NgModule } from '@angular/core';
import { ScriptsComponent } from './scripts.component';
import { SharedModule } from '../../shared/shared.module';
import { ScriptsRoutingModule } from './scripts-routing.module';
import { ScriptListComponent } from './components/script-list/script-list.component';
import { ScriptComponent } from './components/script/script/script.component';

@NgModule({
  declarations: [ScriptsComponent, ScriptListComponent, ScriptComponent],
  imports: [SharedModule, ScriptsRoutingModule]
})
export class ScriptsModule {}
