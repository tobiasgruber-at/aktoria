import {Observable} from 'rxjs';
import {SimpleUser, UserRegistration} from '../../../shared/dtos/user-dtos';
import {ChangePassword} from '../../../shared/dtos/password-change-dto';

export abstract class UserService {
  abstract getOwnUser(): SimpleUser;

  abstract setOwnUser(user: SimpleUser): void;

  abstract getOne(email: string): Observable<SimpleUser>;

  abstract register(req: UserRegistration): Observable<SimpleUser>;

  abstract delete(): Observable<any>;

  abstract update(): Observable<any>;

  abstract resendVerificationEmail(): Observable<void>;

  abstract submitEmailToken(token: string): any;

  abstract forgotPassword(email: string): Observable<void>;

  abstract changePassword(password: ChangePassword): Observable<void>;
}
