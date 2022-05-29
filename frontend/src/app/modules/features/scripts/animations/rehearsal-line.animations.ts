import {animate, AnimationTriggerMetadata, state, style, transition, trigger} from '@angular/animations';
import {voidStyles} from '../../../shared/animations/fixed-appear-animations';

const hiddenStyles: { [property: string]: string | number } = {
  opacity: 0,
  height: 0
};

const transitionDuration = 500;
const transitionCurve = 'cubic-bezier(.42, 0, .09, 1)';
const transitionDelay = 0;

/** Animations, that an element fades out. */
export const lineAppearAnimations: AnimationTriggerMetadata = trigger(
  'lineAppear',
  [
    state('void', style(voidStyles)),
    state('hidden', style(hiddenStyles)),

    transition(':enter', [
      animate(`${transitionDuration}ms ${transitionDelay}ms ${transitionCurve}`)
    ]),
    transition('active => hidden', [
      animate(`${transitionDuration}ms ${transitionCurve}`, style(hiddenStyles))
    ]),
    transition('hidden => active', [
      animate(`${transitionDuration}ms ${transitionCurve}`)
    ])
  ]
);
