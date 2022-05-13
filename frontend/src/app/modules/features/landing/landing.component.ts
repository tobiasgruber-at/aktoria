import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../core/services/auth/auth-service';
import {UserService} from '../../core/services/user/user-service';

/** @author Tobias Gruber */
@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {
  constructor(
    public authService: AuthService,
    public userService: UserService
  ) {}

  ngOnInit() {}
}
