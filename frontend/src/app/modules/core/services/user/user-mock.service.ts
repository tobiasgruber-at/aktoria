import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { SimpleUser, UserRegistration } from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { randomDelay } from '../../../shared/functions/random-delay';
import { ChangePassword } from '../../../shared/dtos/password-change-dto';

@Injectable()
export class UserMockService extends UserService {
  constructor() {
    super();
  }

  $ownUser(): Observable<SimpleUser> {
    return undefined;
  }

  getOwnUser(): SimpleUser {
    return undefined;
  }

  setOwnUser(user: SimpleUser): void {}

  getOne(email: string): Observable<SimpleUser> {
    return of(new SimpleUser(1, 'Max', 'Patternman', 'asdf@asdf.asdf', false));
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

  changePassword(password: ChangePassword): Observable<void> {
    return undefined;
  }

  forgotPassword(email: string): Observable<void> {
    return undefined;
  }

  submitEmailToken(token: string): any {}
}
