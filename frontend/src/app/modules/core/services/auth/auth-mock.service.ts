import {Injectable} from '@angular/core';
import {AuthRequest} from '../../../shared/dtos/auth-request';
import {Observable, of} from 'rxjs';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {AuthService} from './auth-service';

/** @author Tobias Gruber */
@Injectable()
export class AuthMockService extends AuthService {
  public static readonly mockedToken = 'mocked-token';
  private token: string;

  constructor() {
    super();
  }

  loginUser(authRequest: AuthRequest): Observable<string> {
    this.token = AuthMockService.mockedToken;
    return of(this.token);
  }

  isLoggedIn() {
    return !!this.getToken();
  }

  logoutUser() {
    console.log('Logout');
    this.token = '';
  }

  getToken() {
    return this.token;
  }

  getRole() {
    if (this.getToken() != null) {
      return 'ADMIN';
    }
  }

  $loginChanges(): Observable<boolean> {
    return undefined;
  }

  getEmail(): string {
    return '';
  }

  isVerified(): boolean {
    throw new Error('Method not implemented.');
  }
}
