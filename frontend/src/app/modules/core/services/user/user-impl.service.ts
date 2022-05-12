import { Globals } from '../../global/globals';
import { Observable } from 'rxjs';
import { SimpleUser, UserRegistration } from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class UserImplService extends UserService {
  private baseUri: string = this.globals.backendUri + '/users/';
  private user: SimpleUser = null;

  constructor(private globals: Globals, private http: HttpClient) {
    super();
  }

  getOwnUser(): SimpleUser {
    return this.user;
  }

  setOwnUser(user: SimpleUser): void {
    this.user = user;
  }

  getOne(email): Observable<SimpleUser> {
    return this.http.get<SimpleUser>(this.baseUri, { params: { email } });
  }

  register(req: UserRegistration): Observable<SimpleUser> {
    return this.http.post<SimpleUser>(this.baseUri, req);
  }

  update(): Observable<any> {
    return null;
  }

  delete(): Observable<any> {
    return null;
  }

  resendVerificationEmail(): Observable<void> {
    return null;
  }
}
