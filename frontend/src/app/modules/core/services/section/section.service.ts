import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {map, Observable, of} from 'rxjs';
import {CreateSection, SimpleSection} from '../../../shared/dtos/section-dtos';
import {randomDelay} from '../../../shared/functions/random-delay';

@Injectable({
  providedIn: 'root'
})
export class SectionService {
  private baseUri: string = this.globals.backendUri + '/sections';

  constructor(private http: HttpClient, private globals: Globals) {
  }

  /**
   * Gets one section.
   *
   * @param id id of the section.
   * @return section the found section.
   */
  getOne(id: number): Observable<SimpleSection> {
    return this.http
      .get<SimpleSection>(`${this.baseUri}/${id}`)
      .pipe(
        map(
          (section) =>
            new SimpleSection(
              section.name,
              section.startLine,
              section.endLine,
              section.id
            )
        )
      );
  }

  /**
   * Gets all sections.
   *
   * @return observable list of sections
   */
  getAll(scriptId: number): Observable<SimpleSection[]> {
    return this.http
      .get<SimpleSection[]>(this.baseUri, {params: {scriptId}})
      .pipe(
        map((sections) =>
          sections.map(
            (s) => new SimpleSection(s.name, s.startLine, s.endLine, s.id)
          )
        )
      );
  }

  /**
   * Saves a new section.
   *
   * @param section to be saved
   */
  save(section: CreateSection): Observable<SimpleSection> {
    return this.http.post<SimpleSection>(this.baseUri, section);
    /*.pipe(tap((s: DetailedScript) => this.setScripts([...this.scripts, s])));*/
  }

  /**
   * Deletes the specified section.
   *
   * @param id id of script to be deleted
   */
  delete(id: number): Observable<void> {
    return of().pipe(randomDelay());
    /*return this.http.delete<void>(`${this.baseUri}/${id}`);*/
    /*.pipe(
      tap(() => this.setScripts(this.scripts.filter((s) => s.id !== id)))
    );*/
  }
}
