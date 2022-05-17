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
  private scripts: ScriptPreview[] = [];
  private scriptsSubject = new BehaviorSubject<ScriptPreview[]>([]);
  private fullyLoadedScripts: DetailedScript[] = [];

  constructor(private http: HttpClient, private globals: Globals) {}

  /** @return Observable of the scripts. */
  get $scripts(): Observable<ScriptPreview[]> {
    return this.scriptsSubject.asObservable();
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
   * @return observable list of script previews
   */
  getAll(): Observable<ScriptPreview[]> {
    return this.scripts?.length > 0
      ? of(this.scripts)
      : this.http
          .get<ScriptPreview[]>(this.baseUri)
          .pipe(tap((scripts) => this.updateScripts(scripts)));
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
      .pipe(
        tap((s: DetailedScript) => this.updateScripts([...this.scripts, s]))
      );
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
        tap(() => this.updateScripts(this.scripts.filter((s) => s.id !== id)))
      );
  }

  /** Updates scripts and notifies the scripts subject. */
  private updateScripts(scripts): void {
    this.scripts = scripts;
    this.scriptsSubject.next(this.scripts);
  }
}
