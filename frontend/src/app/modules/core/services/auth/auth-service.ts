import { AuthRequest } from '../../../shared/dtos/auth-request';
import { Observable } from 'rxjs';

export abstract class AuthService {
  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  abstract loginUser(authRequest: AuthRequest): Observable<string>;

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  abstract isLoggedIn(): boolean;

  abstract isVerifiedEmail(): boolean;

  abstract logoutUser(): void;

  abstract getToken(): string;

  /**
   * Returns the user role based on the current token
   */
  abstract getUserRole(): string;

  abstract setToken(authResponse: string): void;
}
