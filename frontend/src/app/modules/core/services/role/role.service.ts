import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {MergeRoles, Role} from '../../../shared/dtos/script-dtos';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private baseUri: string = this.globals.backendUri + '/scripts';

  constructor(private http: HttpClient, private globals: Globals) {
  }

  mergeRoles(changes: MergeRoles, scriptId: number): Observable<Role> {
    return this.http.patch<Role>(`${this.baseUri}/${scriptId}/roles`, changes);
  }

  getRole(roleId: number, scriptId: number): Observable<Role> {
    return this.http.get<Role>(`${this.baseUri}/${scriptId}/roles/${roleId}`);
  }
}
