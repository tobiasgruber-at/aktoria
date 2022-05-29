import {Injectable} from '@angular/core';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {DetailedScript, Role} from '../../../shared/dtos/script-dtos';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable()
export class ScriptRehearsalService {
  private script: DetailedScript = null;
  private scriptSubject = new BehaviorSubject<DetailedScript>(this.script);
  private session: SimpleSession = new SimpleSession(
    0,
    2,
    100,
    2,
    new Role(0, 'ALGERNON', null)
  );
  private sessionSubject = new BehaviorSubject<SimpleSession>(this.session);

  get $script(): Observable<DetailedScript> {
    return this.scriptSubject.asObservable();
  }

  get $session(): Observable<SimpleSession> {
    return this.sessionSubject.asObservable();
  }

  setScript(script: DetailedScript): void {
    this.script = script;
    this.scriptSubject.next(this.script);
  }

  setSession(session: SimpleSession): void {
    this.session = session;
    this.sessionSubject.next(this.session);
  }
}
