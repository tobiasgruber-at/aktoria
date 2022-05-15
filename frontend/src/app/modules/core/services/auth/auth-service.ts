import {AuthRequest} from '../../../shared/dtos/auth-request';
import {Observable} from 'rxjs';

export abstract class AuthService {
  /** @return Observable that notifies on login-state changes. */
  abstract $loginChanges(): Observable<boolean>;

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  abstract loginUser(authRequest: AuthRequest): Observable<string>;

  abstract logoutUser(): void;

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  abstract isLoggedIn(): boolean;

  abstract getToken(): string;

  abstract getEmail(): string;

  /**
   * Returns the user role based on the current token
   */
  abstract getRole(): string;
}
