import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HelpersService {
  private mainScrollPosY = 0;
  private mainScrollPosYSubject = new BehaviorSubject<number>(
    this.mainScrollPosY
  );

  get $mainScrollPosY(): Observable<number> {
    return this.mainScrollPosYSubject.asObservable();
  }

  setMainScrollPosY(posY: number): void {
    this.mainScrollPosY = posY;
    this.mainScrollPosYSubject.next(this.mainScrollPosY);
  }
}
