import { Component, Input, OnInit } from '@angular/core';
import { SessionService } from '../../../../core/services/session/session.service';
import { SimpleSession } from '../../../../shared/dtos/session-dtos';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.scss']
})
export class SessionListComponent implements OnInit {
  @Input() title: string;
  sessions: SimpleSession[];

  constructor(public sessionService: SessionService) {}

  ngOnInit(): void {
    this.sessionService.getAll().subscribe({
      next: (res) => {
        this.sessions = res;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
