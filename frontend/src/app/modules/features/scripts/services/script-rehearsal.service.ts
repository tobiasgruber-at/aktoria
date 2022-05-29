import { Injectable } from '@angular/core';
import { SimpleSession } from '../../../shared/dtos/session-dtos';
import {
  DetailedScript,
  Role,
  SimpleScript
} from '../../../shared/dtos/script-dtos';
import { BehaviorSubject, Observable } from 'rxjs';

export interface ScriptSelectedRoleMapping {
  [scriptId: number]: number;
}

const scriptSelectedRoleMappingLSKey = 'scriptSelectedRoleMapping';

@Injectable()
export class ScriptRehearsalService {
  private script: DetailedScript = null;
  private scriptSubject = new BehaviorSubject<DetailedScript>(this.script);
  private session: SimpleSession = null;
  private sessionSubject = new BehaviorSubject<SimpleSession>(this.session);
  private selectedRoles: ScriptSelectedRoleMapping = localStorage.getItem(
    scriptSelectedRoleMappingLSKey
  )
    ? JSON.parse(localStorage.getItem(scriptSelectedRoleMappingLSKey))
    : {};
  private selectedRoleSubject = new BehaviorSubject<ScriptSelectedRoleMapping>(
    this.selectedRoles
  );

  get $script(): Observable<DetailedScript> {
    return this.scriptSubject.asObservable();
  }

  get $session(): Observable<SimpleSession> {
    return this.sessionSubject.asObservable();
  }

  get $selectedRole(): Observable<ScriptSelectedRoleMapping> {
    return this.selectedRoleSubject.asObservable();
  }

  setScript(script: DetailedScript): void {
    this.script = script;
    this.scriptSubject.next(this.script);
  }

  setSession(session: SimpleSession): void {
    this.session = session;
    this.sessionSubject.next(this.session);
  }

  setSelectedRole(script: SimpleScript, role: Role): void {
    this.selectedRoles[script.getId()] = role.id;
    this.selectedRoleSubject.next(this.selectedRoles);
    localStorage.setItem(
      scriptSelectedRoleMappingLSKey,
      JSON.stringify(this.selectedRoles)
    );
  }
}
