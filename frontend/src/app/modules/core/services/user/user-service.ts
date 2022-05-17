import { Observable } from 'rxjs';
import {
  DetailedUser,
  SimpleUser,
  UpdateUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';
import { ChangePassword } from '../../../shared/dtos/password-change-dto';

export abstract class UserService {
  abstract $ownUser(): Observable<SimpleUser>;

  abstract getOwnUser(): SimpleUser;

  abstract setOwnUser(user: SimpleUser): void;

  abstract getOne(email: string): Observable<SimpleUser>;

  abstract register(req: UserRegistration): Observable<SimpleUser>;

  abstract delete(id: number): Observable<void>;

  abstract update(user: UpdateUser): Observable<DetailedUser>;

  abstract resendVerificationEmail(): Observable<void>;

  abstract submitEmailToken(token: string): any;

  abstract forgotPassword(email: string): Observable<void>;

  abstract changePassword(password: ChangePassword): Observable<void>;
}
