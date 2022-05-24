import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { Globals } from '../../global/globals';
import {
  DetailedScript,
  ScriptPreview,
  SimpleScript
} from '../../../shared/dtos/script-dtos';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ScriptService {
  private baseUri: string = this.globals.backendUri + '/scripts';
  /** Script that the user parsed, but is not saved yet. */
  private stagedScript: SimpleScript = null;
  private stagedScriptSubject = new BehaviorSubject<SimpleScript>(null);
  private scripts: ScriptPreview[] = [];
  private scriptsSubject = new BehaviorSubject<ScriptPreview[]>([]);
  private fullyLoadedScripts: DetailedScript[] = [];

  constructor(private http: HttpClient, private globals: Globals) {}

  get $stagedScript(): Observable<SimpleScript> {
    const cachedScript = localStorage.getItem('stagedScript');
    return cachedScript
      ? of(JSON.parse(cachedScript))
      : this.stagedScriptSubject.asObservable();
  }

  /** @return Observable of the scripts. */
  get $scripts(): Observable<ScriptPreview[]> {
    return this.scriptsSubject.asObservable();
  }

  /** Sets the staged script and notifies the staged-script subject. */
  setStagedScript(script: SimpleScript): void {
    this.stagedScript = script;
    localStorage.setItem('stagedScript', JSON.stringify(script));
    this.stagedScriptSubject.next(script);
  }

  /**
   * Gets one script.
   *
   * @param id id of the script
   * @return script the found script.
   */
  getOne(id: number): Observable<DetailedScript> {
    const loadedScript = this.fullyLoadedScripts.find((f) => f.id === id);
    return loadedScript
      ? of(loadedScript)
      : this.http.get<DetailedScript>(`${this.baseUri}/${id}`).pipe(
          tap((script) => {
            this.fullyLoadedScripts.push(script);
          })
        );
  }

  /**
   * Gets all script previews
   *
   *
   * @return observable list of script previews
   */
  getAll(): Observable<ScriptPreview[]> {
    const uri = this.baseUri;
    return this.scripts?.length > 0
      ? of(this.scripts)
      : this.http
          .get<ScriptPreview[]>(uri)
          .pipe(tap((scripts) => this.setScripts(scripts)));
  }

  /**
   * Parses a new script.
   *
   * @param file the script to be posted
   * @param startPage start page for parsing
   */
  parse(file: File, startPage: number): Observable<SimpleScript> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('startPage', '' + startPage);
    return this.http.post<SimpleScript>(this.baseUri + '/new', formData);
  }

  /**
   * Saves a new script.
   *
   * @param script to be saved
   */
  save(script): Observable<DetailedScript> {
    return this.http
      .post<DetailedScript>(this.baseUri, script)
      .pipe(tap((s: DetailedScript) => this.setScripts([...this.scripts, s])));
  }

  /**
   * Deletes the specified script
   *
   * @param id id of script to be deleted
   */
  delete(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUri}/${id}`)
      .pipe(
        tap(() => this.setScripts(this.scripts.filter((s) => s.id !== id)))
      );
  }

  /**
   * Sends an invite to join the script.
   *
   * @param email the email the invite is sent to
   * @param scriptId the scriptId of the invitation
   */
  inviteParticipant(email: string, scriptId: string): Observable<void> {
    return this.http
      .post<void>(this.baseUri + '/' + scriptId + '/invitations', email);
  }

  /**
   * Adds a new participant to the script
   *
   * @param token token of invitation
   * @param scriptId id of the script
   */
  addParticipant(token: string, scriptId: string): Observable<void> {
    return this.http
      .post<void>(this.baseUri + '/' + scriptId + '/participants', token);
  }

  /** Resets the state of this service. */
  resetState(): void {
    this.setScripts([]);
    this.setStagedScript(null);
    this.fullyLoadedScripts = [];
    localStorage.setItem('stagedScript', null);
  }

  /** Sets scripts and notifies the scripts subject. */
  private setScripts(scripts): void {
    this.scripts = scripts;
    this.scriptsSubject.next(this.scripts);
  }
}
