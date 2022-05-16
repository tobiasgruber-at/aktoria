import {animate, AnimationTriggerMetadata, state, style, transition, trigger} from '@angular/animations';

const voidStyles: { [property: string]: string | number } = {
  opacity: 0,
  transform: 'scale(.85)',
  margin: 0,
  height: 0
};
const transitionDuration = 250;
const transitionCurve = 'cubic-bezier(.42, 0, .09, 1)';
const transitionDelay = 0;

export const lightAppearAnimations: AnimationTriggerMetadata = trigger(
  'lightAppear',
  [
    state('void', style(voidStyles)),

    transition(':enter', [
      animate(`${transitionDuration}ms ${transitionDelay}ms ${transitionCurve}`)
    ]),
    transition(':leave', [
      animate(`${transitionDuration}ms ${transitionCurve}`, style(voidStyles))
    ])
  ]
);
