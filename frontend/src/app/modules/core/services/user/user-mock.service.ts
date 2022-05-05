import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
  AuthenticatedUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { randomDelay } from '../../../shared/functions/random-delay';
import { tap } from 'rxjs/operators';
import { AuthService } from '../auth/auth-service';

@Injectable()
export class UserMockService extends UserService {
  readonly mockedToken: string = 'test';

  constructor(private authService: AuthService) {
    super();
  }

  delete(): Observable<any> {
    return null;
  }

  register(user: UserRegistration): Observable<AuthenticatedUser> {
    return of(
      new AuthenticatedUser(1, user.name, user.email, false, this.mockedToken)
    ).pipe(
      randomDelay(),
      tap((res: AuthenticatedUser) => this.authService.setToken(res.jwtToken))
    );
  }

  update(): Observable<any> {
    return null;
  }

  resendVerificationEmail(): Observable<void> {
    return of(null).pipe(randomDelay());
  }
}
