import {
  Component,
  HostBinding,
  HostListener,
  Input,
  OnDestroy,
  OnInit
} from '@angular/core';
import { ScriptViewerService } from '../../services/script-viewer.service';
import { Subject, takeUntil } from 'rxjs';
import { SimpleScript } from '../../../../shared/dtos/script-dtos';
import { SimpleSection } from '../../../../shared/dtos/section-dtos';
import { SimpleSession } from '../../../../shared/dtos/session-dtos';
import { ScriptRehearsalService } from '../../services/script-rehearsal.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rehearsal-section',
  templateUrl: './rehearsal-section.component.html',
  styleUrls: ['./rehearsal-section.component.scss']
})
export class RehearsalSectionComponent implements OnInit, OnDestroy {
  @Input() isActive = false;
  @Input() section: SimpleSection = null;
  /** Whether this is just a create-new section button, or an existing session. */
  @Input() isCreate = false;
  script: SimpleScript = null;
  private $destroy = new Subject<void>();

  constructor(
    private scriptViewerService: ScriptViewerService,
    private scriptRehearsalService: ScriptRehearsalService,
    private router: Router
  ) {}

  get startLinePercentage(): number {
    return this.script && this.section
      ? (this.section.startLine.index / this.script.getLastLineIdx()) * 100
      : 0;
  }

  get endLinePercentage(): number {
    return this.script && this.section
      ? (this.section.endLine.index / this.script?.getLastLineIdx()) * 100
      : 0;
  }

  @HostBinding('class')
  private get classes(): string[] {
    const classes = ['mb-2'];
    if (this.isActive) {
      classes.push('is-active');
    }
    return classes;
  }

  ngOnInit(): void {
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => (this.script = script));
    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe(
        (markedSection) =>
          (this.isActive = this.section === markedSection?.section)
      );
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  startRehearsal(): void {
    // TODO: create rehearsal
    this.scriptRehearsalService.setSession(
      new SimpleSession(
        null,
        null,
        null,
        this.section,
        this.section.startLine.index,
        this.script.roles[0]
      )
    );
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}/rehearse`);
  }

  @HostListener('click', ['$event'])
  private selectSection(): void {
    this.scriptViewerService.setMarkedSection(
      this.isActive || this.isCreate
        ? null
        : {
            section: this.section,
            scrollTo: true
          }
    );
  }
}
