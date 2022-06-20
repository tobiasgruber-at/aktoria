import {Injectable} from '@angular/core';
import {AuthRequest} from '../../../shared/dtos/auth-request';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../../global/globals';
import {DecodedToken} from '../../../shared/interfaces/decoded-token';
import {UserService} from '../user/user.service';

/** LocalStorage key for the token. */
const tokenLSKey = 'authToken';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly authBaseUri = this.globals.backendUri + '/authentication';
  private loginChangesSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
    private userService: UserService
  ) {
  }

  /** @return Observable that notifies on login-state changes. */
  get $loginChanges(): Observable<boolean> {
    return this.loginChangesSubject.asObservable();
  }

  /**
   * Logs the user in and stores the jwt on success.
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => {
          this.updateLoginState(authResponse);
        })
      );
  }

  /** Logs the user out, by removing the jwt. */
  logoutUser() {
    this.updateLoginState();
  }

  /** Checks if a valid JWT token is saved in the localStorage. */
  isLoggedIn() {
    return (
      !!this.getToken() &&
      this.getTokenExpirationDate(this.getToken()).valueOf() >
      new Date().valueOf()
    );
  }

  /** Checks if the users email is verified. */
  isVerified(): boolean {
    const role = this.getRole();
    return (
      role === 'ADMIN' ||
      role === 'VERIFIED' ||
      this.userService.ownUser?.verified
    );
  }

  /** Gets the logged-in users jwt token. */
  getToken() {
    return localStorage.getItem('authToken');
  }

  /** Gets the email of the logged-in user from the session. */
  getEmail(): string {
    const decoded: DecodedToken = jwt_decode(this.getToken());
    return decoded?.sub;
  }

  /** Returns the user role based on the current token. */
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
      localStorage.removeItem(tokenLSKey);
    } else {
      localStorage.setItem(tokenLSKey, token);
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
