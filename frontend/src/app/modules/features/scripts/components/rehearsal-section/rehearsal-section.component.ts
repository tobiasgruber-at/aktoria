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
import { CreateSession } from '../../../../shared/dtos/session-dtos';
import {
  ScriptRehearsalService,
  ScriptSelectedRoleMapping
} from '../../services/script-rehearsal.service';
import { Router } from '@angular/router';
import { SessionService } from '../../../../core/services/session/session.service';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { Theme } from '../../../../shared/enums/theme.enum';

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
  private selectedRoles: ScriptSelectedRoleMapping = {};
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
      .subscribe((roles) => {
        this.selectedRoles = roles;
        if (!this.selectedRoles) {
          this.router.navigateByUrl('/scripts');
          this.toastService.show({
            message: 'Keine Rolle ausgewählt',
            theme: Theme.danger
          });
        }
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  startRehearsal(): void {
    const selectedRoleID = this.selectedRoles?.[this.script.getId()];
    const session: CreateSession = {
      sectionId: this.section.id,
      roleId: selectedRoleID
    };
    this.sessionService.save(session).subscribe({
      next: (s) => {
        s.setSection(this.section);
        s.setScript(this.script);
        this.scriptRehearsalService.setSession(s);
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
