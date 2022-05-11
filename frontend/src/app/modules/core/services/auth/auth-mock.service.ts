import { Injectable } from '@angular/core';
import { AuthRequest } from '../../../shared/dtos/auth-request';
import { Observable, of } from 'rxjs';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import { AuthService } from './auth-service';

/** @author Tobias Gruber */
@Injectable()
export class AuthMockService extends AuthService {
  public static readonly mockedToken = 'mocked-token';
  private token: string;

  constructor() {
    super();
  }

  loginUser(authRequest: AuthRequest): Observable<string> {
    this.setToken(AuthMockService.mockedToken);
    return of(AuthMockService.mockedToken);
  }

  isLoggedIn() {
    //TODO: return !!this.getToken();
    return true;
  }

  isVerifiedEmail() {
    return false;
  }

  logoutUser() {
    console.log('Logout');
    this.token = '';
  }

  getToken() {
    return this.token;
  }

  getUserRole() {
    if (this.getToken() != null) {
      return 'ADMIN';
    }
  }

  setToken(authRes: string) {
    this.token = authRes;
  }
}
