import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../../global/globals';
import { Observable } from 'rxjs';
import { Line, UpdateLine } from '../../../shared/dtos/script-dtos';

@Injectable({
  providedIn: 'root'
})
export class LineService {
  private baseUri: string = this.globals.backendUri + '/lines';

  constructor(private http: HttpClient, private globals: Globals) {}

  patchLine(line: UpdateLine, lineId: number): Observable<Line> {
    return this.http.patch<Line>(`${this.baseUri}/${lineId}`, line);
  }
}
