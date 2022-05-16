import { Globals } from '../../global/globals';
import { Observable } from 'rxjs';
import {
  DetailedUser,
  SimpleUser,
  UpdateUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChangePassword } from 'src/app/modules/shared/dtos/password-change-dto';

@Injectable()
export class UserImplService extends UserService {
  private baseUri: string = this.globals.backendUri + '/users';
  private ownUser: SimpleUser = null;

  constructor(private globals: Globals, private http: HttpClient) {
    super();
  }

  getOwnUser(): SimpleUser {
    return this.ownUser;
  }

  setOwnUser(user: SimpleUser): void {
    this.ownUser = user;
  }

  getOne(email): Observable<SimpleUser> {
    return this.http.get<SimpleUser>(this.baseUri, { params: { email } });
  }

  register(req: UserRegistration): Observable<SimpleUser> {
    return this.http.post<SimpleUser>(this.baseUri, req);
  }

  update(user: UpdateUser): Observable<DetailedUser> {
    return this.http.patch<DetailedUser>(this.baseUri + '/' + user.id, user);
  }

  delete(): Observable<any> {
    return null;
  }

  resendVerificationEmail(): Observable<void> {
    return this.http.post<void>(this.baseUri + '/tokens', null);
  }

  submitEmailToken(token: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/verification', token);
  }

  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/reset-password', email);
  }

  changePassword(password: ChangePassword): Observable<void> {
    return this.http.put<void>(this.baseUri + '/change-password', password);
  }
}
