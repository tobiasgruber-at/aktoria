import { NgModule } from '@angular/core';
import { MessageComponent } from './message.component';
import { MessageRoutingModule } from './message-routing.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [MessageComponent],
  imports: [SharedModule, MessageRoutingModule]
})
export class MessageModule {}
