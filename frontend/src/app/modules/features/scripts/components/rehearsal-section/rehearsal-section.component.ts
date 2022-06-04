import {Component, HostBinding, HostListener, Input, OnDestroy, OnInit} from '@angular/core';
import {ScriptViewerService} from '../../services/script-viewer.service';
import {Subject, takeUntil} from 'rxjs';
import {Role, SimpleScript} from '../../../../shared/dtos/script-dtos';
import {SimpleSection} from '../../../../shared/dtos/section-dtos';
import {CreateSession} from '../../../../shared/dtos/session-dtos';
import {ScriptRehearsalService} from '../../services/script-rehearsal.service';
import {Router} from '@angular/router';
import {SessionService} from '../../../../core/services/session/session.service';
import {ToastService} from '../../../../core/services/toast/toast.service';

/** Presents information of a rehearsal section. */
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
  private selectedRole: Role = null;
  private $destroy = new Subject<void>();

  constructor(
    private scriptViewerService: ScriptViewerService,
    private scriptRehearsalService: ScriptRehearsalService,
    private router: Router,
    private toastService: ToastService,
    private sessionService: SessionService
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
      .subscribe((markedSection) => {
        this.isActive = this.section === markedSection?.section;
      });
    this.scriptRehearsalService.$selectedRole
      .pipe(takeUntil(this.$destroy))
      .subscribe((role) => {
        this.selectedRole = role;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  /** Starts a new rehearsal with this section. */
  startRehearsal(): void {
    const session: CreateSession = {
      sectionId: this.section.id,
      roleId: this.selectedRole?.id
    };
    this.sessionService.start(session).subscribe({
      next: (createdSession) => {
        createdSession.init(this.script, this.section);
        this.scriptRehearsalService.setSession(createdSession);
        this.router.navigateByUrl(`/scripts/${this.script?.getId()}/rehearse`);
      },
      error: (err) => {
        this.toastService.showError(err);
      }
    });
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
