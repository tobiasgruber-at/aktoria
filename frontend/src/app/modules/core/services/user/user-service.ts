import { Observable } from 'rxjs';
import {
  AuthenticatedUser,
  UserRegistration
} from '../../../shared/dtos/user-dtos';

export abstract class UserService {
  abstract register(req: UserRegistration): Observable<AuthenticatedUser>;

  abstract delete(): Observable<any>;

  abstract update(): Observable<any>;

  abstract resendVerificationEmail(): Observable<void>;
}
