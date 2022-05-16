import { Component, OnInit } from '@angular/core';
import { UserService } from '../../core/services/user/user-service';
import { SimpleUser } from '../../shared/dtos/user-dtos';
import { AuthService } from '../../core/services/auth/auth-service';
import { appearAnimations } from '../../shared/animations/appear-animations';
import { ActivatedRoute, Router } from '@angular/router';
import { Theme } from '../../shared/enums/theme.enum';
import { ToastService } from '../../core/services/toast/toast.service';
import { FormBase } from '../../shared/classes/form-base';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  animations: [appearAnimations]
})
export class ProfileComponent extends FormBase implements OnInit {
  user: SimpleUser;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private formBuilder: FormBuilder
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.getUser();
    this.route.paramMap.subscribe(() => this.getUser());

    this.form = this.formBuilder.group({
      id: [null]
    });
  }

  getUser() {
    this.authService.$loginChanges().subscribe((loggedIn) => {
      if (loggedIn) {
        this.user = this.userService.getOwnUser();
      }
    });
  }

  protected sendSubmit() {
    this.userService.delete(this.user.id).subscribe({
      next: (res) => {
        this.toastService.show({
          message: 'Erfolgreich gelöscht!',
          theme: Theme.primary
        });
        this.authService.logoutUser();
        this.router.navigateByUrl('/login').then();
      },
      error: (err) => this.handleError(err)
    });
  }
}
