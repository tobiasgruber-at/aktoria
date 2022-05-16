import { Injectable } from '@angular/core';
import { AuthRequest } from '../../../shared/dtos/auth-request';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import { Globals } from '../../global/globals';
import { AuthService } from './auth-service';
import { DecodedToken } from '../../../shared/interfaces/decoded-token';
import {UserService} from '../user/user-service';

@Injectable()
export class AuthImplService extends AuthService {
  private readonly authBaseUri = this.globals.backendUri + '/authentication';
  /** LocalStorage key for the token */
  private readonly tokenLSKey = 'authToken';
  private loginChangesSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  constructor(private httpClient: HttpClient, private globals: Globals, private userService: UserService) {
    super();
  }

  $loginChanges(): Observable<boolean> {
    return this.loginChangesSubject.asObservable();
  }

  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri, authRequest, { responseType: 'text' })
      .pipe(
        tap((authResponse: string) => {
          this.updateLoginState(authResponse);
        })
      );
  }

  logoutUser() {
    this.updateLoginState();
  }

  isLoggedIn() {
    return (
      !!this.getToken() &&
      this.getTokenExpirationDate(this.getToken()).valueOf() >
        new Date().valueOf()
    );
  }

  isVerified(): boolean {
    const role = this.getRole();
    return ((role === 'ADMIN') || (role === 'VERIFIED')) || (this.userService.getOwnUser()?.verified);
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  getEmail(): string {
    const decoded: DecodedToken = jwt_decode(this.getToken());
    return decoded?.sub;
  }

  getRole() {
    if (this.getToken() != null) {
      const decoded: DecodedToken = jwt_decode(this.getToken());
      const authInfo = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_VERIFIED')) {
        return 'VERIFIED';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  /**
   * Updates the login state.
   *
   * @description If a token is passed, a loggedIn event is emitted, otherwise (if null is passed) a loggedOut event.
   */
  private updateLoginState(token: string = null): void {
    if (token === null) {
      localStorage.removeItem(this.tokenLSKey);
    } else {
      localStorage.setItem(this.tokenLSKey, token);
    }
    this.loginChangesSubject.next(this.isLoggedIn());
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: DecodedToken = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }
    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
