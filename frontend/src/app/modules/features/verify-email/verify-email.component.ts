import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserRegistration} from '../../shared/dtos/user-dtos';
import {Theme} from '../../shared/enums/theme.enum';
import {UserService} from '../../core/services/user/user-service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {

  token: string;
  status: string;
  content: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.status = '...';
    this.content = '...';
    this.submitToken();
  }

  private submitToken() {
    this.userService
      .submitEmailToken(this.token)
      .subscribe({
        next: (res) => {
          this.status = 'Emailverifizierung erfolgreich';
          this.content = 'Deine Emailadresse wurde bestätigt';
        },
        error: (err) => {
          this.status = 'Emailverifizierung fehlgeschlagen';
          this.content = 'Der übertragene Token ist abgelaufen oder ungültig';
        }
      });
  }

}
