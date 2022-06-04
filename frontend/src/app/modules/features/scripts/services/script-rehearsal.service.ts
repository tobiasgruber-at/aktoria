import { Injectable } from '@angular/core';
import { SimpleSession } from '../../../shared/dtos/session-dtos';
import { DetailedScript, Role } from '../../../shared/dtos/script-dtos';
import { BehaviorSubject, map, Observable } from 'rxjs';

export interface ScriptSelectedRoleMapping {
  [scriptId: number]: number;
}

/** LocalStorage key for the mapping of selected roles of the respective scripts. */
const scriptSelectedRoleMappingLSKey = 'scriptSelectedRoleMapping';

/**
 * Service for script rehearsals.<br>
 * Includes states for the selection of role and section as well as rehearsal info.
 */
@Injectable()
export class ScriptRehearsalService {
  private scriptSubject = new BehaviorSubject<DetailedScript>(null);
  private sessionSubject = new BehaviorSubject<SimpleSession>(null);
  private selectedRoleSubject = new BehaviorSubject<ScriptSelectedRoleMapping>(
    localStorage.getItem(scriptSelectedRoleMappingLSKey)
      ? JSON.parse(localStorage.getItem(scriptSelectedRoleMappingLSKey))
      : {}
  );

  /** @see script */
  get $script(): Observable<DetailedScript> {
    return this.scriptSubject.asObservable();
  }

  get $session(): Observable<SimpleSession> {
    return this.sessionSubject.asObservable();
  }

  get $selectedRole(): Observable<Role> {
    return this.selectedRoleSubject.asObservable().pipe(
      map((scriptSelectedRoles) => {
        const script = this.scriptSubject.getValue();
        if (!script) {
          return null;
        }
        const selectedRoleId = scriptSelectedRoles[script?.getId()];
        return script.roles.find((r) => r.id === selectedRoleId);
      })
    );
  }

  setScript(script: DetailedScript): void {
    this.scriptSubject.next(script);
    // notify roles, as $selectedRole can only be evaluated once the script is loaded
    this.selectedRoleSubject.next(this.selectedRoleSubject.getValue());
  }

  setSession(session: SimpleSession): void {
    this.sessionSubject.next(session);
  }

  setSelectedRole(role: Role): void {
    const selectedRoles = this.selectedRoleSubject.getValue();
    selectedRoles[this.scriptSubject.getValue()?.getId()] = role?.id;
    this.selectedRoleSubject.next(selectedRoles);
    localStorage.setItem(
      scriptSelectedRoleMappingLSKey,
      JSON.stringify(selectedRoles)
    );
  }
}
