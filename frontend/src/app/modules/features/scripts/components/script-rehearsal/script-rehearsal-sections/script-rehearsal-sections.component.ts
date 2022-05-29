import { Component, OnDestroy, OnInit } from '@angular/core';
import { SimpleScript } from '../../../../../shared/dtos/script-dtos';
import {
  IsMarkingSection,
  ScriptViewerService
} from '../../../services/script-viewer.service';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';
import { FormBase } from '../../../../../shared/classes/form-base';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { SectionService } from '../../../../../core/services/section/section.service';

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
export class ScriptRehearsalSectionsComponent
  extends FormBase
  implements OnInit, OnDestroy {
  getLoading = true;
  script: SimpleScript = null;
  sections: SimpleSection[];
  curStep: Step = /* Step.createSection ||*/ Step.selectSection;
  isMarkingSection: IsMarkingSection = null;
  markedSection: SimpleSection = null;
  readonly steps = Step;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private scriptService: ScriptService,
    private sectionService: SectionService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((markedSection) => {
        this.markedSection = markedSection?.section;
      });
    this.scriptViewerService.$isMarkingSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((isMarkingSection) => {
        this.isMarkingSection = isMarkingSection;
      });
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
        this.sectionService
          .getAll(this.script.getId())
          .subscribe((sections) => {
            this.sections = sections;
          });
      });
    this.form = this.formBuilder.group({
      sectionName: ['', [Validators.required, Validators.maxLength(100)]]
    });
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      this.scriptService.getOne(id).subscribe({
        next: (script) => {
          this.scriptViewerService.setScript(script);
          this.getLoading = false;
        },
        error: (err) => {
          this.handleError(err);
          this.router.navigateByUrl('/scripts');
        }
      });
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
    }
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  protected processSubmit(): void {
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}/rehearse`);
  }
}
