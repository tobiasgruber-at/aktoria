import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SimpleUser, UserRegistration } from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { randomDelay } from '../../../shared/functions/random-delay';
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

  register(user: UserRegistration): Observable<SimpleUser> {
    return of(
      new SimpleUser(1, user.firstName, user.lastName, user.email, false)
    );
  }

  update(): Observable<any> {
    return null;
  }

  resendVerificationEmail(): Observable<void> {
    return of(null).pipe(randomDelay());
  }
}
