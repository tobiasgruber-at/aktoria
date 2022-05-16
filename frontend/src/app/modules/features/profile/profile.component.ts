import { Component, OnInit } from '@angular/core';
import { UserService } from '../../core/services/user/user-service';
import { SimpleUser } from '../../shared/dtos/user-dtos';
import { AuthService } from '../../core/services/auth/auth-service';
import { appearAnimations } from '../../shared/animations/appear-animations';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  animations: [appearAnimations]
})
export class ProfileComponent implements OnInit {
  user: SimpleUser;

  constructor(
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    this.authService.$loginChanges().subscribe((loggedIn) => {
      if (loggedIn) {
        this.user = this.userService.getOwnUser();
      }
    });
  }

  onDelete() {}
}
