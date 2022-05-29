import {
  Component,
  ElementRef,
  HostBinding,
  Input,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Line } from '../../../../../shared/dtos/script-dtos';
import { ScriptRehearsalService } from '../../../services/script-rehearsal.service';
import { Subject, takeUntil } from 'rxjs';
import { SimpleSession } from '../../../../../shared/dtos/session-dtos';

@Component({
  selector: 'app-rehearsal-line',
  templateUrl: './rehearsal-line.component.html',
  styleUrls: ['./rehearsal-line.component.scss']
})
export class RehearsalLineComponent implements OnInit, OnDestroy {
  @ViewChild('content') contentRef: ElementRef;
  @Input() line: Line = null;
  session: SimpleSession = null;
  private $destroy = new Subject<void>();

  constructor(private scriptRehearsalService: ScriptRehearsalService) {}

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.line.index === this.session.currentLine) {
      classes.push('is-active');
    }
    if (this.line.roles.some((r) => r.name === this.session.role.name)) {
      classes.push('is-highlighted');
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
}
