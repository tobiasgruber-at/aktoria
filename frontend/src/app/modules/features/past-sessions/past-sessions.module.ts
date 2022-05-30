import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PastSessionsComponent } from './past-sessions.component';
import { PastSessionsRoutingModule } from './past-sessions-routing.module';
import { SharedModule } from '../../shared/shared.module';
import { SessionListComponent } from './components/session-list/session-list.component';
import { SessionListItemComponent } from './components/session-list/session-list-item/session-list-item.component';

@NgModule({
  declarations: [
    PastSessionsComponent,
    SessionListComponent,
    SessionListItemComponent
  ],
  imports: [CommonModule, PastSessionsRoutingModule, SharedModule]
})
export class PastSessionsModule {}
