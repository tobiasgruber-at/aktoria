import { Component, OnDestroy, OnInit } from '@angular/core';
import { Line, SimpleScript } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { Subject, takeUntil } from 'rxjs';
import { SectionService } from '../../../../../core/services/section/section.service';
import { ScriptRehearsalService } from '../../../services/script-rehearsal.service';
import { Theme } from '../../../../../shared/enums/theme.enum';

enum Step {
  selectSection,
  createSection
}

@Component({
  selector: 'app-script-rehearsal-sections',
  templateUrl: './script-rehearsal-sections.component.html',
  styleUrls: ['./script-rehearsal-sections.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptRehearsalSectionsComponent implements OnInit, OnDestroy {
  getLoading = true;
  script: SimpleScript = null;
  sections: SimpleSection[];
  curStep: Step = Step.selectSection;
  readonly steps = Step;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    public scriptRehearsalService: ScriptRehearsalService,
    private scriptService: ScriptService,
    private sectionService: SectionService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      this.scriptService.getOne(id).subscribe({
        next: (script) => {
          this.scriptViewerService.setScript(script);
          this.scriptRehearsalService.setScript(script);
          // subscribe here to selected role, as script is needed to evaluate it
          this.scriptRehearsalService.$selectedRole
            .pipe(takeUntil(this.$destroy))
            .subscribe((role) => {
              if (!role) {
                this.router.navigateByUrl('/scripts');
                this.toastService.show({
                  message: 'Keine Rolle ausgewählt',
                  theme: Theme.danger
                });
              }
              this.scriptViewerService.setSelectedRole(role);
            });
        },
        error: (err) => {
          this.toastService.showError(err);
          this.router.navigateByUrl('/scripts');
        }
      });
    });

    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
        if (this.script) {
          this.sectionService.getAll(this.script?.getId()).subscribe({
            next: (sections) => {
              this.sections = sections;
              this.getLoading = false;
            },
            error: (err) => {
              this.toastService.showError(err);
              this.router.navigateByUrl('/scripts');
            }
          });
        }
      });
  }

  changeStep(step: Step): void {
    this.curStep = step;
    if (this.curStep === Step.createSection) {
      this.scriptViewerService.setIsMarkingSection('start');
      this.scriptViewerService.setMarkedSection({
        section: new SimpleSection(
          null,
          new Line(null, null, null, null, null, null, null),
          new Line(Infinity, null, null, null, null, null, null)
        ),
        scrollTo: false
      });
    } else if (this.curStep === Step.selectSection) {
      this.scriptViewerService.setIsMarkingSection(null);
      this.scriptViewerService.setMarkedSection(null);
    }
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  backToOverview(): void {
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}`);
  }
}
