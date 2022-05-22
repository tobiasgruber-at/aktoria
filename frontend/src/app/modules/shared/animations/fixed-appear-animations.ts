import {
  animate,
  AnimationTriggerMetadata,
  state,
  style,
  transition,
  trigger
} from '@angular/animations';

const voidStyles: { [property: string]: string | number } = {
  opacity: 0,
  transform: 'translateY(20px)'
};
const transitionDuration = 550;
const transitionCurve = 'cubic-bezier(.42, 0, .09, 1)';
const transitionDelay = 0;

/** Animations, that an element fades in from the bottom. */
export const fixedAppearAnimations: AnimationTriggerMetadata = trigger(
  'fixedAppear',
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
