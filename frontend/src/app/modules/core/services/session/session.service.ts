import {Injectable} from '@angular/core';
import {Globals} from '../../global/globals';
import {HttpClient} from '@angular/common/http';
import {map, Observable, of} from 'rxjs';
import {CreateSession, SimpleSession, UpdateSession} from '../../../shared/dtos/session-dtos';
import {Cache} from '../../../shared/interfaces/cache';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private baseUri: string = this.globals.backendUri + '/sessions';
  private cachedSessions: Cache<SimpleSession> = {};

  constructor(private http: HttpClient, private globals: Globals) {
  }

  /**
   * Gets the session with the specified ID.
   *
   * @param id the ID of the session to be returned
   * @return the session with the specified ID
   */
  getOne(id: number): Observable<SimpleSession> {
    const loadedSession = this.cachedSessions[id];
    return loadedSession
      ? of(loadedSession)
      : this.http.get<SimpleSession>(`${this.baseUri}/${id}`).pipe(
        map(this.mapSessionInterfaceToClass),
        tap((session) => {
          this.cachedSessions[session.id] = session;
        })
      );
  }

  patchOne(
    id: number,
    updateSession: UpdateSession
  ): Observable<SimpleSession> {
    return this.http
      .patch<SimpleSession>(`${this.baseUri}/${id}`, updateSession)
      .pipe(map(this.mapSessionInterfaceToClass));
  }

  /**
   * Gets all sessions associated with the logged-in user.
   *
   * @return all sessions associated with the logged-in user.
   */
  getAll(): Observable<SimpleSession[]> {
    return this.http
      .get<SimpleSession[]>(this.baseUri)
      .pipe(map((sessions) => sessions.map(this.mapSessionInterfaceToClass)));
  }

  /**
   * Saves a new session
   *
   * @param session to be saved
   */
  start(session: CreateSession): Observable<SimpleSession> {
    return this.http
      .post<SimpleSession>(this.baseUri, session)
      .pipe(map(this.mapSessionInterfaceToClass));
  }

  endSession(id: number): Observable<SimpleSession> {
    return this.http
      .post<SimpleSession>(`${this.baseUri}/${id}`, null)
      .pipe(map(this.mapSessionInterfaceToClass));
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

  /** Maps a fetched session interface to a class. */
  private mapSessionInterfaceToClass(session: SimpleSession): SimpleSession {
    return new SimpleSession(
      session.id,
      session.start,
      session.end,
      session.selfAssessment,
      session.deprecated,
      session.coverage,
      session.sectionId,
      session.currentLineIndex,
      session.role
    );
  }
}
