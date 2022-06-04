import {
  Component,
  EventEmitter,
  HostBinding,
  OnDestroy,
  OnInit,
  Output
} from '@angular/core';
import { FormBase } from '../../../../../../shared/classes/form-base';
import { ToastService } from '../../../../../../core/services/toast/toast.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import {
  IsMarkingSection,
  ScriptViewerService
} from '../../../../services/script-viewer.service';
import { Role, SimpleScript } from '../../../../../../shared/dtos/script-dtos';
import {
  CreateSection,
  SimpleSection
} from '../../../../../../shared/dtos/section-dtos';
import { SessionService } from '../../../../../../core/services/session/session.service';
import { SectionService } from '../../../../../../core/services/section/section.service';
import { UserService } from '../../../../../../core/services/user/user-service';
import { SimpleUser } from '../../../../../../shared/dtos/user-dtos';
import { CreateSession } from '../../../../../../shared/dtos/session-dtos';
import { ScriptRehearsalService } from '../../../../services/script-rehearsal.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rehearsal-section-create',
  templateUrl: './rehearsal-section-create.component.html',
  styleUrls: ['./rehearsal-section-create.component.scss']
})
export class RehearsalSectionCreateComponent
  extends FormBase
  implements OnInit, OnDestroy {
  @Output() private cancel = new EventEmitter<void>();
  @HostBinding('class') private classes = 'd-flex flex-column flex-grow-1';
  markedSection: SimpleSection = null;
  isMarkingSection: IsMarkingSection = null;
  script: SimpleScript = null;
  private $destroy = new Subject<void>();
  private user: SimpleUser = null;
  private selectedRole: Role = null;

  constructor(
    private formBuilder: FormBuilder,
    private toastService: ToastService,
    public scriptViewerService: ScriptViewerService,
    private sessionService: SessionService,
    private sectionService: SectionService,
    private userService: UserService,
    private scriptRehearsalService: ScriptRehearsalService,
    private router: Router
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      sectionName: ['', [Validators.required, Validators.maxLength(100)]],
      startLine: [null, [Validators.required]],
      endLine: [null, Validators.required]
    });
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((markedSection) => {
        this.markedSection = markedSection?.section;
      });
    this.scriptViewerService.$isMarkingSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((isMarkingSection) => {
        const { startLine, endLine } = this.markedSection;
        if (this.isMarkingSection === 'start' && isMarkingSection === 'end') {
          this.form.patchValue({ startLine });
        } else if (
          this.isMarkingSection === 'end' &&
          isMarkingSection === null
        ) {
          this.form.patchValue({ endLine });
        }
        this.isMarkingSection = isMarkingSection;
      });
    this.userService
      .$ownUser()
      .pipe(takeUntil(this.$destroy))
      .subscribe((user) => {
        this.user = user;
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

  onCancel(): void {
    this.cancel.emit();
  }

  protected processSubmit(): void {
    const { sectionName, startLine, endLine } = this.form.value;
    const section: CreateSection = new CreateSection(
      sectionName,
      this.user?.id,
      startLine.id,
      endLine.id,
      []
    );
    this.sectionService.save(section).subscribe({
      next: (createdSection) => {
        const session: CreateSession = {
          sectionId: createdSection.id,
          roleId: this.selectedRole?.id
        };
        this.sessionService.start(session).subscribe({
          next: (createdSession) => {
            createdSession.init(this.script, createdSection);
            this.scriptRehearsalService.setSession(createdSession);
            this.router.navigateByUrl(
              `/scripts/${this.script?.getId()}/rehearse`
            );
          },
          error: (err) => this.handleError(err)
        });
      },
      error: (err) => this.handleError(err)
    });
  }
}
