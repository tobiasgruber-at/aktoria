import { Injectable } from '@angular/core';
import { Globals } from '../../global/globals';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { SimpleSession } from '../../../shared/dtos/session-dtos';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private baseUri: string = this.globals.backendUri + '/session';

  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Gets the session with the specified ID.
   *
   * @param id the ID of the session to be returned
   * @return the session with the specified ID
   */
  getOne(id: number): Observable<SimpleSession> {
    return this.http
      .get<SimpleSession>(this.baseUri + '/' + id)
      .pipe(
        map(
          (session) =>
            new SimpleSession(
              session.id,
              session.start,
              session.end,
              session.selfAssessment,
              session.deprecated,
              session.coverage,
              session.sectionId,
              session.currentLine,
              session.role
            )
        )
      );
  }

  /**
   * Gets all sessions associated with the logged-in user.
   *
   * @return all sessions associated with the logged-in user.
   */
  getAll(): Observable<SimpleSession[]> {
    return this.http
      .get<SimpleSession[]>(this.baseUri /*TODO+ '/past'*/)
      .pipe(
        map((sessions) =>
          sessions.map(
            (s) =>
              new SimpleSession(
                s.id,
                s.start,
                s.end,
                s.selfAssessment,
                s.deprecated,
                s.coverage,
                s.sectionId,
                s.currentLine,
                s.role
              )
          )
        )
      );
  }

  /**
   * Saves a new session
   *
   * @param session to be saved
   */
  save(session: SimpleSession): Observable<SimpleSession> {
    //TODO
    return null;
  }

  /**
   * Deletes the session with the specified ID
   *
   * @param id the ID of the session to be deleted
   */
  delete(id: number): Observable<void> {
    //TODO
    return null;
  }
}
