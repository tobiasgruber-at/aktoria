import { Injectable } from '@angular/core';
import { SimpleSession } from '../../../shared/dtos/session-dtos';
import { DetailedScript, Role } from '../../../shared/dtos/script-dtos';
import { BehaviorSubject, map, Observable } from 'rxjs';

export interface ScriptSelectedRoleMapping {
  [scriptId: number]: number;
}

const scriptSelectedRoleMappingLSKey = 'scriptSelectedRoleMapping';

/**
 * Service for script rehearsals.<br>
 * Includes states for the selection of role and section as well as rehearsal info.
 */
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

  get $selectedRole(): Observable<Role> {
    return this.selectedRoleSubject.asObservable().pipe(
      map((scriptSelectedRoles) => {
        if (!this.script) {
          return null;
        }
        const selectedRoleId = scriptSelectedRoles[this.script?.getId()];
        return this.script.roles.find((r) => r.id === selectedRoleId);
      })
    );
  }

  setScript(script: DetailedScript): void {
    this.script = script;
    this.scriptSubject.next(this.script);
    // notify roles, as $selectedRole can only be evaluated once the script is loaded
    this.selectedRoleSubject.next(this.selectedRoles);
  }

  setSession(session: SimpleSession): void {
    this.session = session;
    this.sessionSubject.next(this.session);
  }

  setSelectedRole(role: Role): void {
    this.selectedRoles[this.script?.getId()] = role?.id;
    this.selectedRoleSubject.next(this.selectedRoles);
    localStorage.setItem(
      scriptSelectedRoleMappingLSKey,
      JSON.stringify(this.selectedRoles)
    );
  }
}
