import { Globals } from '../../global/globals';
import { BehaviorSubject, Observable } from 'rxjs';
import {
  SimpleUser,
  UpdateUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChangePassword } from 'src/app/modules/shared/dtos/password-change-dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUri: string = this.globals.backendUri + '/users';
  private ownUserSubject = new BehaviorSubject<SimpleUser>(null);

  constructor(private globals: Globals, private http: HttpClient) {}

  /** @return Observable of the own user. */
  get $ownUser(): Observable<SimpleUser> {
    return this.ownUserSubject.asObservable();
  }

  /** @return Snapshot of the own user. */
  get ownUser(): SimpleUser {
    return this.ownUserSubject.getValue();
  }

  /** Sets the own user. */
  setOwnUser(user: SimpleUser): void {
    this.ownUserSubject.next(user);
  }

  /** Gets the own user. */
  getOne(email): Observable<SimpleUser> {
    return this.http.get<SimpleUser>(this.baseUri, { params: { email } });
  }

  /**
   * Registeres a new user.
   *
   * @param req The user registration body.
   */
  register(req: UserRegistration): Observable<SimpleUser> {
    return this.http.post<SimpleUser>(this.baseUri, req);
  }

  /**
   * Updates a user.
   *
   * @param user Body of the updated user.
   */
  update(user: UpdateUser): Observable<SimpleUser> {
    return this.http.patch<SimpleUser>(this.baseUri + '/' + user.id, user);
  }

  /** Deletes a user. */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUri + '/' + id);
  }

  /** Resends a verification email. */
  resendVerificationEmail(): Observable<void> {
    return this.http.post<void>(this.baseUri + '/tokens', null);
  }

  /** Verifies the user with the email token. */
  submitEmailToken(token: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/verification', token);
  }

  /** Sends a forgotten-password request. */
  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(this.baseUri + '/forgot-password', email);
  }

  /** Changes the user password. */
  changePassword(password: ChangePassword): Observable<void> {
    return this.http.put<void>(this.baseUri + '/reset-password', password);
  }

  /** Resets the state of this service. */
  resetState(): void {
    this.setOwnUser(null);
  }
}
