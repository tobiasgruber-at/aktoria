import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, map, Observable, of} from 'rxjs';
import {Globals} from '../../global/globals';
import {
  DetailedScript,
  Line,
  Page,
  RawDetailedScript, RawLine, RawPage,
  RawSimpleScript,
  ScriptPreview,
  SimpleScript
} from '../../../shared/dtos/script-dtos';
import {tap} from 'rxjs/operators';
import {AuthService} from '../auth/auth.service';
import {Cache} from '../../../shared/interfaces/cache';

// eslint-disable-next-line prefer-arrow/prefer-arrow-functions
function convertToBlob(audio: string) {

  if (audio === null || audio === undefined) {
    return null;
  }

  const data = audio.split('base64,');

  const b64toBlob = (b64Data, contentType = '', sliceSize=512) => {
    const byteCharacters = atob(b64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);

      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);

      byteArrays.push(byteArray);
    }

    const blob = new Blob(byteArrays, {type: contentType});
    return blob;
  };

  return b64toBlob(data[1]);
}

@Injectable({
  providedIn: 'root'
})
export class ScriptService {
  private baseUri: string = this.globals.backendUri + '/scripts';
  /** Script that the user parsed, but that isn't saved yet. */
  private stagedScriptSubject = new BehaviorSubject<SimpleScript>(null);
  private scripts: ScriptPreview[] = [];
  private scriptsSubject = new BehaviorSubject<ScriptPreview[]>([]);
  private cachedScripts: Cache<DetailedScript> = {};

  constructor(
    private http: HttpClient,
    private globals: Globals,
    private authService: AuthService
  ) {
  }

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
    const loadedScript = this.cachedScripts[id];
    return loadedScript
      ? of(loadedScript)
      : this.http.get<RawDetailedScript>(`${this.baseUri}/${id}`).pipe(
        map(this.mapScriptInterfaceToClass),
        tap((script) => {
          this.cachedScripts[script.id] = script;
        })
      );
  }

  /**
   * Gets all script previews.
   *
   * @return observable list of script previews
   */
  getAll(): Observable<ScriptPreview[]> {
    return this.scripts?.length > 0
      ? of(this.scripts)
      : this.http.get<ScriptPreview[]>(this.baseUri).pipe(
        map((scripts) =>
          scripts.map((s) => new ScriptPreview(s.id, s.name, s.owner))
        ),
        tap((scripts) => this.setScripts(scripts))
      );
  }

  /**
   * Gets the script of which a section is practiced in the session with the given ID.
   *
   * @param id the id of the session
   * @return the script in which the section practiced in the session with the specified ID lies
   */
  getScriptBySession(id): Observable<DetailedScript> {
    return this.http
      .get<RawDetailedScript>(this.baseUri + '/session?id=' + id)
      .pipe(map(this.mapScriptInterfaceToClass));
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
    return this.http
      .post<RawSimpleScript>(this.baseUri + '/new', formData)
      .pipe(
        map(
          (script) => new SimpleScript(this.convertAudioOfPages(script.pages),
            script.roles, script.name)
        )
      );
  }

  /**
   * Saves a new script.
   *
   * @param script to be saved
   */
  save(script: SimpleScript): Observable<DetailedScript> {
    return this.http
      .post<DetailedScript>(this.baseUri, script)
      .pipe(tap((s: DetailedScript) => this.setScripts([...this.scripts, s])));
  }

  /**
   * Deletes the specified script.
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
    return this.http.post<void>(
      this.baseUri + '/' + scriptId + '/invitations',
      email
    );
  }

  /**
   * Adds a new participant to the script.
   *
   * @param token token of invitation
   * @param scriptId id of the script
   */
  addParticipant(token: string, scriptId: string): Observable<void> {
    return this.http.post<void>(
      this.baseUri + '/' + scriptId + '/participants',
      token
    );
  }

  removeParticipant(scriptId, email: string): Observable<void> {
    if (email === this.authService.getEmail()) {
      return this.http
        .delete<void>(this.baseUri + '/' + scriptId + '/participants/' + email)
        .pipe(
          tap(() =>
            this.setScripts(this.scripts.filter((s) => s.id !== scriptId))
          )
        );
    } else {
      return this.http.delete<void>(
        this.baseUri + '/' + scriptId + '/participants/' + email
      );
    }
  }

  inviteLink(scriptId): Observable<ArrayBuffer> {
    return this.http.post<ArrayBuffer>(
      this.baseUri + '/' + scriptId + '/inviteLink',
      null,
      // @ts-ignore
      {responseType: 'text'}
    );
  }

  /** Resets the state of this service. */
  resetState(): void {
    this.setScripts([]);
    this.setStagedScript(null);
    this.cachedScripts = {};
    localStorage.setItem('stagedScript', null);
  }

  /** Sets scripts and notifies the scripts subject. */
  private setScripts(scripts): void {
    this.scripts = scripts;
    this.scriptsSubject.next(this.scripts);
  }

  /** Maps a fetched script to a class. */
  private mapScriptInterfaceToClass(script: RawDetailedScript): DetailedScript {
    return new DetailedScript(
      script.id,
      script.owner,
      script.participants.sort((a, b) =>
        a.firstName < b.firstName ? -1 : a.firstName > b.firstName ? 1 : 0
      ),
      script.pages.map(
        (page) =>
          new Page(
            page.index,
            page.lines.map(
              (line) =>
                new Line(
                  line.index,
                  line.roles,
                  line.content,
                  convertToBlob(line.audio),
                  line.recordedBy,
                  line.active,
                  line.conflictType,
                  line.id
                )
            )
          )
      ),
      script.roles.sort((a, b) =>
        a.name < b.name ? -1 : a.name > b.name ? 1 : 0
      ),
      script.name
    );
  }

  private convertAudioOfPages(rawPages: RawPage[]): Page[] {
    const pages: Page[] = [];
    for (const rawPage of rawPages) {
      const page = new Page(rawPage.index, this.convertAudioOfLines(rawPage.lines));
      pages.push(page);
    }
    return pages;
  }

  private convertAudioOfLines(rawLines: RawLine[]): Line[] {
    const lines: Line[] = [];
    for (const rawLine of rawLines) {
      const line = new Line(rawLine.index, rawLine.roles, rawLine.content, convertToBlob(rawLine.audio),
        rawLine.recordedBy, rawLine.active, rawLine.conflictType, rawLine.id);
      lines.push(line);
    }
    return lines;
  }
}
