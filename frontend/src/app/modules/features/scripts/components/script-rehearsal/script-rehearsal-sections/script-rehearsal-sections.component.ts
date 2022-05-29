import { Component, OnDestroy, OnInit } from '@angular/core';
import { SimpleScript } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { Subject, takeUntil } from 'rxjs';
import { SectionService } from '../../../../../core/services/section/section.service';
import { ScriptRehearsalService } from '../../../services/script-rehearsal.service';
import { SimpleSession } from '../../../../../shared/dtos/session-dtos';

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
  markedSection: SimpleSection = null;
  readonly steps = Step;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private scriptRehearsalService: ScriptRehearsalService,
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
          this.getLoading = false;
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
          this.sectionService
            .getAll(this.script?.getId())
            .subscribe((sections) => {
              this.sections = sections;
            });
        }
      });

    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((markedSection) => {
        this.markedSection = markedSection?.section;
      });
  }

  changeStep(step: Step): void {
    this.curStep = step;
    if (this.curStep === Step.createSection) {
      this.scriptViewerService.setIsMarkingSection('start');
      this.scriptViewerService.setMarkedSection({
        section: new SimpleSection(null, null, Infinity),
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

  startRehearsal(): void {
    this.scriptRehearsalService.setSession(
      new SimpleSession(
        null,
        this.markedSection.startLine,
        this.markedSection.endLine,
        this.markedSection.startLine,
        this.script.roles[0]
      )
    );
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}/rehearse`);
  }

  backToOverview(): void {
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}`);
  }
}
