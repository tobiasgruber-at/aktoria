import {Component, ElementRef, HostBinding, Input, OnChanges, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Line} from '../../../../../shared/dtos/script-dtos';
import {ScriptRehearsalService} from '../../../services/script-rehearsal.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {RehearsalControlsComponent} from '../rehearsal-controls/rehearsal-controls.component';
import {appearAnimations} from '../../../../../shared/animations/appear-animations';

/** Line of a script within the script rehearsal. */
@Component({
  selector: 'app-rehearsal-line',
  templateUrl: './rehearsal-line.component.html',
  styleUrls: ['./rehearsal-line.component.scss'],
  animations: [appearAnimations]
})
export class RehearsalLineComponent implements OnInit, OnDestroy, OnChanges {
  @ViewChild('content') contentRef: ElementRef;
  @Input() line: Line = null;
  @Input() isBlurred = false;
  session: SimpleSession = null;
  rehearsalControls = RehearsalControlsComponent;
  isRecording = false;
  isRecordingMode = false;
  private $destroy = new Subject<void>();

  constructor(public scriptRehearsalService: ScriptRehearsalService) {}

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isCurLine) {
      classes.push('is-active');
    }
    if (this.line.roles?.length < 1) {
      classes.push('is-instruction');
    }
    return classes;
  }

  /** @return Whether this line is spoken by the users role. */
  get isOwnLine(): boolean {
    return this.line.roles.some((r) => r.name === this.session.role?.name);
  }

  /** @return Whether this line is the current one. */
  get isCurLine(): boolean {
    return this.line.index === this.session.currentLineIndex;
  }

  /** @return Whether this line is currently recorded. */
  get isRecordingLine(): boolean {
    return this.isRecordingMode && this.isRecording && this.isCurLine;
  }

  ngOnInit(): void {
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
    this.scriptRehearsalService.$isRecordingMode
      .pipe(takeUntil(this.$destroy))
      .subscribe((isRecordingMode) => {
        this.isRecordingMode = isRecordingMode;
      });
    this.scriptRehearsalService.$isRecording
      .pipe(takeUntil(this.$destroy))
      .subscribe((isRecording) => {
        this.isRecording = isRecording;
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
