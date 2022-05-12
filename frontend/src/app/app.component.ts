import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from './modules/core/services/auth/auth-service';
import { UserService } from './modules/core/services/user/user-service';
import { Subject, takeUntil } from 'rxjs';
import { ToastService } from './modules/core/services/toast/toast.service';
import { Theme } from './modules/shared/enums/theme.enum';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private $destroy = new Subject<void>();

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.handleOwnUser();
  }

  ngOnDestroy(): void {
    this.$destroy.next();
    this.$destroy.complete();
  }

  /** Handles own-user by fetching the data once logged in. */
  private handleOwnUser(): void {
    this.authService
      .$loginChanges()
      .pipe(takeUntil(this.$destroy))
      .subscribe((loggedIn) => {
        if (!loggedIn) {
          this.userService.setOwnUser(null);
          return;
        }
        this.userService
          .getOne(this.authService.getEmail())
          .pipe(takeUntil(this.$destroy))
          .subscribe({
            next: (user) => {
              this.userService.setOwnUser(user);
            },
            error: (err) => {
              this.authService.logoutUser();
              this.toastService.show({
                message: 'Ungültige Sitzung, bitte melden Sie sich erneut an.',
                theme: Theme.danger
              });
            }
          });
      });
  }
}
