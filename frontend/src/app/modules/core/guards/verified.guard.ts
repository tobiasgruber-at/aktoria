import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthService} from '../services/auth/auth-service';

/** Guard to ensure that the user is verified. */
@Injectable({
  providedIn: 'root'
})
export class VerifiedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log(this.authService.isVerified());
    if (this.authService.isVerified()) {
      return true;
    } else {
      console.log(this.authService.getRole());
      this.router.navigate(['/']);
      return false;
    }
  }
}
