import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from '../services/auth/auth-service';

/** Guard to ensure that the user is logged in and verified. */
@Injectable({
  providedIn: 'root'
})
export class VerifiedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log(this.authService.isVerified());
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    } else if (!this.authService.isVerified()) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
}
