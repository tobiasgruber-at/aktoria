import {Component, Input, OnInit} from '@angular/core';
import {SessionService} from '../../../../core/services/session/session.service';
import {SimpleSession} from '../../../../shared/dtos/session-dtos';

@Component({
  selector: 'app-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.scss']
})
export class SessionListComponent implements OnInit {
  @Input() title: string;
  unfinishedSessions: SimpleSession[];
  finishedSessions: SimpleSession[];

  constructor(public sessionService: SessionService) {
  }

  ngOnInit(): void {
    const us = [];
    const fs = [];
    this.sessionService.getAll().subscribe({
      next: (res) => {
        res.forEach((session) => {
          if (session.end || session.selfAssessment) {
            fs.push(session);
          } else {
            us.push(session);
          }
        });
        this.finishedSessions = fs;
        this.unfinishedSessions = us;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
