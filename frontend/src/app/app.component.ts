import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from './modules/core/services/auth/auth-service';
import { UserService } from './modules/core/services/user/user-service';
import { Subject, takeUntil } from 'rxjs';
import { ToastService } from './modules/core/services/toast/toast.service';
import { Theme } from './modules/shared/enums/theme.enum';
import { ScriptService } from './modules/core/services/script/script.service';

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
    private scriptService: ScriptService,
    private toastService: ToastService
  ) {}

  ngOnInit() {
    this.handleLoginChanges();
  }

  ngOnDestroy(): void {
    this.$destroy.next();
    this.$destroy.complete();
  }

  /**
   * Reacts accordingly to logins or logouts.
   *
   * @description <strong>On Login: </strong> Extracts the user's email out of the jwt token and
   * fetches the corresponding user from the backend.<br>
   * <strong>On Logout: </strong> Resets state of all services.
   */
  private handleLoginChanges(): void {
    this.authService
      .$loginChanges()
      .pipe(takeUntil(this.$destroy))
      .subscribe((loggedIn) => {
        if (!loggedIn) {
          this.userService.resetState();
          this.scriptService.resetState();
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
