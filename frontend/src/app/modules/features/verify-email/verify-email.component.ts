import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../core/services/user/user-service';
import { AuthService } from '../../core/services/auth/auth-service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  successful: boolean = null;
  private token: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.submitToken();
  }

  private submitToken() {
    this.userService.submitEmailToken(this.token).subscribe({
      next: () => {
        this.successful = true;
        this.authService.logoutUser();
      },
      error: () => {
        this.successful = false;
      }
    });
  }
}
