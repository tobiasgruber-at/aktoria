import { Globals } from '../../global/globals';
import { Observable } from 'rxjs';
import { SimpleUser, UserRegistration } from '../../../shared/dtos/user-dtos';
import { UserService } from './user-service';
import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable()
export class UserImplService extends UserService {
  private baseUri: string = this.globals.backendUri + '/users';

  constructor(private globals: Globals, private http: HttpClient) {
    super();
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
    return this.http.post<void>(this.baseUri + '/verificationToken', null);
  }

  submitEmailToken(token: string): any  {
    return this.http.post<void>(this.baseUri + '/submitToken', token);
  }
}
