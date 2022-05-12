import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../core/services/user/user-service';
import { SimpleUser } from '../../shared/dtos/user-dtos';

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
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.status = '...';
    this.content = '...';
    this.submitToken();
  }

  private submitToken() {
    this.userService.submitEmailToken(this.token).subscribe({
      next: (res) => {
        this.status = 'Emailverifikation erfolgreich';
        this.content = 'Deine Emailadresse wurde bestätigt';
        const updatedOwnUser: SimpleUser = {
          ...this.userService.getOwnUser(),
          verified: true
        };
        this.userService.setOwnUser(updatedOwnUser);
      },
      error: (err) => {
        this.status = 'Emailverifikation fehlgeschlagen';
        this.content = 'Der übertragene Token ist abgelaufen oder ungültig';
      }
    });
  }
}
