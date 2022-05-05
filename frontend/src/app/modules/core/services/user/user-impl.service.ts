import { Globals } from '../../global/globals';
import { Observable } from 'rxjs';
import {
  AuthenticatedUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { Injectable } from '@angular/core';

@Injectable()
export class UserImplService extends UserService {
  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private globals: Globals) {
    super();
  }

  register(req: UserRegistration): Observable<AuthenticatedUser> {
    return null;
  }

  update(): Observable<any> {
    return null;
  }

  delete(): Observable<any> {
    return null;
  }

  resendVerificationEmail(): Observable<void> {
    return null;
  }
}
