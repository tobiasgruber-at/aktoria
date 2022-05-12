import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {AuthService} from '../services/auth/auth-service';
import {UserService} from '../services/user/user-service';

/** @author Tobias Gruber */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private globals: Globals
  ) {}

  /** Intercepts outgoing http requests and adds authorizations headers if necessary. */
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let adaptedReq = req;
    if (!this.isBlacklistedRequest(req)) {
      adaptedReq = req.clone({
        headers: req.headers.set(
          'Authorization',
          'Bearer ' + this.authService.getToken()
        )
      });
    }
    return next.handle(adaptedReq);
  }

  /**
   * Checks if the request is blacklisted.
   *
   * @description Authorization headers won't be appended to blacklisted requests.
   */
  private isBlacklistedRequest(req: HttpRequest<any>): boolean {
    const blacklistedEndpoints = [];
    if (req.method === 'GET') {
      blacklistedEndpoints.push('/authentication');
    } else if (req.method === 'POST') {
      blacklistedEndpoints.push('/users');
      blacklistedEndpoints.push('/users/verification');
    }

    return blacklistedEndpoints.some(
      (e) => req.url === this.globals.backendUri + e
    );
  }
}
