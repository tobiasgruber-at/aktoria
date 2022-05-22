import { Observable } from 'rxjs';
import {
  DetailedUser,
  SimpleUser,
  UpdateUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { ChangePassword } from '../../../shared/dtos/password-change-dto';

export abstract class UserService {
  /** @return Observable of the own user. */
  abstract $ownUser(): Observable<SimpleUser>;

  /** @return Snapshot of the own user. */
  abstract getOwnUser(): SimpleUser;

  /** Sets the own user. */
  abstract setOwnUser(user: SimpleUser): void;

  /** Gets the own user. */
  abstract getOne(email: string): Observable<SimpleUser>;

  /**
   * Registeres a new user.
   *
   * @param req The user registration body.
   */
  abstract register(req: UserRegistration): Observable<SimpleUser>;

  /** Deletes a user. */
  abstract delete(id: number): Observable<void>;

  /**
   * Updates a user.
   *
   * @param user Body of the updated user.
   */
  abstract update(user: UpdateUser): Observable<DetailedUser>;

  /** Resends a verification email. */
  abstract resendVerificationEmail(): Observable<void>;

  /** Verifies the user with the email token. */
  abstract submitEmailToken(token: string): any;

  /** Sends a forgotten-password request. */
  abstract forgotPassword(email: string): Observable<void>;

  /** Changes the user password. */
  abstract changePassword(password: ChangePassword): Observable<void>;

  /** Resets the state of this service. */
  abstract resetState(): void;
}
