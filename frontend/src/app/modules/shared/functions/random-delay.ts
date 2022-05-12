import { MonoTypeOperatorFunction } from 'rxjs';
import { delay } from 'rxjs/operators';

export const randomDelay = <T>(): MonoTypeOperatorFunction<T> =>
  delay(500 + Math.random() * 500);
