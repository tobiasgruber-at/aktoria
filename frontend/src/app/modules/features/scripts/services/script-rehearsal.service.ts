import {Injectable} from '@angular/core';
import {SimpleSession} from '../../../shared/dtos/session-dtos';
import {DetailedScript, Line, Role} from '../../../shared/dtos/script-dtos';
import {BehaviorSubject, map, Observable, Subscription} from 'rxjs';
import {VoiceRecordingService} from './voice-recording.service';

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
  /** Whether generally recording mode is active (does not imply, that a line is currently recorded). */
  private isRecordingModeSubject = new BehaviorSubject<boolean>(false);
  /** Whether a line is currently recorded. */
  private isRecordingSubject = new BehaviorSubject<boolean>(false);

  constructor(private voiceRecordingService: VoiceRecordingService) {
    let curLineIdxSubscription: Subscription;
    this.sessionSubject.subscribe((session) => {
      curLineIdxSubscription?.unsubscribe();
      if (session) {
        curLineIdxSubscription = session.$currentLineIndex.subscribe(() => {
          const curLine: Line = session.getCurrentLine();
          /*if (
            curLine.roles?.some(
              (r) =>
                r.name === this.selectedRoleSubject.getValue()?.name
            )
          ) {
          }*/
        });
      }
    });
  }

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

  /** @see isRecordingModeSubject */
  get $isRecordingMode(): Observable<boolean> {
    return this.isRecordingModeSubject.asObservable();
  }

  /** @see isRecordingSubject */
  get $isRecording(): Observable<boolean> {
    return this.isRecordingSubject.asObservable();
  }

  /**
   * Starts recording mode.
   *
   * @throws AudioNotSupportedError If browser doesn't support microphone access
   * @throws AudioNotAllowedError If user rejected microphone access
   */
  async startRecordingMode(): Promise<void> {
    if (this.isRecordingModeSubject.getValue()) {
      return;
    }
    await this.voiceRecordingService.requestPermissions();
    this.isRecordingModeSubject.next(true);
  }

  /** Stops recording mode. */
  stopRecordingMode(): void {
    this.isRecordingModeSubject.next(false);
  }

  setScript(script: DetailedScript): void {
    this.scriptSubject.next(script);
    // notify roles, as $selectedRole can only be evaluated once the script is loaded
    this.selectedRoleSubject.next(this.selectedRoleSubject.getValue());
  }

  setSession(session: SimpleSession): void {
    this.sessionSubject.next(session);
  }

  /** Sets a selected role and caches it in the local storage. */
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
