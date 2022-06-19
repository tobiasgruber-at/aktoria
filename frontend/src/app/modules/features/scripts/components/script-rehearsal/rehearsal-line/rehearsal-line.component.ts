import {Component, ElementRef, HostBinding, Input, OnChanges, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Line} from '../../../../../shared/dtos/script-dtos';
import {ScriptRehearsalService} from '../../../services/script-rehearsal.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {RehearsalControlsComponent} from '../rehearsal-controls/rehearsal-controls.component';

/** Line of a script within the script rehearsal. */
@Component({
  selector: 'app-rehearsal-line',
  templateUrl: './rehearsal-line.component.html',
  styleUrls: ['./rehearsal-line.component.scss']
})
export class RehearsalLineComponent implements OnInit, OnDestroy, OnChanges {
  @ViewChild('content') contentRef: ElementRef;
  @Input() line: Line = null;
  @Input() isBlurred = false;
  session: SimpleSession = null;
  rehearsalControls = RehearsalControlsComponent;
  private $destroy = new Subject<void>();


  constructor(private scriptRehearsalService: ScriptRehearsalService) {
  }

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.line.index === this.session.currentLineIndex) {
      classes.push('is-active');
    }
    if (this.line.roles?.length < 1) {
      classes.push('is-instruction');
    }
    return classes;
  }

  ngOnInit(): void {
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  ngOnChanges(changes) {
    this.isBlurred = changes.isBlurred.currentValue;
  }
}
