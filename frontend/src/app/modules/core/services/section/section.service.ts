import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../../global/globals';
import { Observable, of } from 'rxjs';
import {
  CreateSection,
  SimpleSection
} from '../../../shared/dtos/section-dtos';
import { randomDelay } from '../../../shared/functions/random-delay';

@Injectable({
  providedIn: 'root'
})
export class SectionService {
  private baseUri: string = this.globals.backendUri + '/scripts/sections';

  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Gets one section.
   *
   * @param id id of the section.
   * @return section the found section.
   */
  getOne(id: number): Observable<SimpleSection> {
    return of(null);
    /*return this.http
      .get<SimpleSection>(`${this.baseUri}/${id}`)
      .pipe(
        map(
          (section) =>
            new SimpleSection(section.name, section.startLine, section.endLine)
        )
      );*/
  }

  /**
   * Gets all sections.
   *
   * @return observable list of sections
   */
  getAll(scriptId: number): Observable<SimpleSection[]> {
    return of([
      new SimpleSection(
        'Kapitel 1',
        {
          index: 3,
          roles: [],
          content: 'test',
          active: true,
          audioSnippet: null
        },
        {
          index: 40,
          roles: [],
          content: 'test',
          active: true,
          audioSnippet: null
        }
      ),
      new SimpleSection(
        'Kapitel 2',
        {
          index: 3,
          roles: [],
          content: 'test',
          active: true,
          audioSnippet: null
        },
        {
          index: 40,
          roles: [],
          content: 'test',
          active: true,
          audioSnippet: null
        }
      )
    ]).pipe(randomDelay());
    /*return this.http
      .get<SimpleSection[]>(this.baseUri)
      .pipe(
        map((sections) =>
          sections.map((s) => new SimpleSection(s.name, s.startLine, s.endLine))
        )
      );*/
  }

  /**
   * Saves a new section.
   *
   * @param section to be saved
   */
  save(section: CreateSection): Observable<SimpleSection> {
    return of(null).pipe(randomDelay());
    /*return this.http.post<DetailedScript>(this.baseUri, section);*/
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
