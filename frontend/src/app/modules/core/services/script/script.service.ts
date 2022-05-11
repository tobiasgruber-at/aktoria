import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../../global/globals';
import {
  DeleteScriptRequest,
  SimpleScript,
  UploadScript
} from '../../../shared/dtos/script-dtos';

@Injectable({
  providedIn: 'root'
})
export class ScriptService {
  private baseUri: string = this.globals.backendUri + '/scripts';

  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Gets all scripts
   *
   * @return observable list of scripts
   */
  getAll(): Observable<SimpleScript[]> {
    return this.http.get<SimpleScript[]>(this.baseUri);
  }

  /**
   * Posts a new script
   *
   * @param script the script to be posted
   */
  post(script): Observable<UploadScript> {
    return this.http.post<UploadScript>(this.baseUri + '/new', script);
  }

  /**
   * Deletes the specified script
   *
   * @param script the script to be deleted
   */
  delete(script: DeleteScriptRequest): Observable<void> {
    return null;
    //TODO: find correct URL
    // return this.http.delete<DeleteScriptRequest>(this.baseUri+)
  }
}
