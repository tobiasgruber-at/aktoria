import { AuthRequest } from '../../../shared/dtos/auth-request';
import { Observable } from 'rxjs';

export abstract class AuthService {
  /** @return Observable that notifies on login-state changes. */
  abstract $loginChanges(): Observable<boolean>;

  /**
   * Logs the user in.
   *
   * @param authRequest User data
   */
  abstract loginUser(authRequest: AuthRequest): Observable<string>;

  /** Logs the user out. */
  abstract logoutUser(): void;

  /** Checks if a valid JWT token is saved in the localStorage. */
  abstract isLoggedIn(): boolean;

  /** Checks if the users email is verified. */
  abstract isVerified(): boolean;

  /** Gets the logged-in users jwt token. */
  abstract getToken(): string;

  /** Gets the email of the logged-in user from the session. */
  abstract getEmail(): string;

  /** Returns the user role based on the current token. */
  abstract getRole(): string;
}
