import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from '../services/auth/auth-service';

@Injectable({
  providedIn: 'root'
})
export class NotVerifiedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log(!this.authService.isVerified());
    if (!this.authService.isVerified()) {
      return true;
    } else {
      this.router.navigate(['/scripts']);
      return false;
    }
  }

}
