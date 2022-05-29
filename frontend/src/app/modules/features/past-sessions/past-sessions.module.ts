import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PastSessionsComponent } from './past-sessions.component';
import { PastSessionsRoutingModule } from './past-sessions-routing.module';

@NgModule({
  declarations: [PastSessionsComponent],
  imports: [CommonModule, PastSessionsRoutingModule]
})
export class PastSessionsModule {}
