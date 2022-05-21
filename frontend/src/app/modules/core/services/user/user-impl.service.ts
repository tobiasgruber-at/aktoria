import { Globals } from '../../global/globals';
import { BehaviorSubject, Observable } from 'rxjs';
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
  private ownUserSubject = new BehaviorSubject<SimpleUser>(null);

  constructor(private globals: Globals, private http: HttpClient) {
    super();
  }

  $ownUser(): Observable<SimpleUser> {
    return this.ownUserSubject.asObservable();
  }

  getOwnUser(): SimpleUser {
    return this.ownUser;
  }

  setOwnUser(user: SimpleUser): void {
    this.ownUser = user;
    this.ownUserSubject.next(user);
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

  delete(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUri + '/' + id);
  }

  resendVerificationEmail(): Observable<void> {
    return this.http.post<void>(this.baseUri + '/tokens', null);
  }

  submitEmailToken(token: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/verification', token);
  }

  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/forgot-password', email);
  }

  changePassword(password: ChangePassword): Observable<void> {
    return this.http.put<void>(this.baseUri + '/reset-password', password);
  }

  resetState(): void {
    this.setOwnUser(null);
  }
}
