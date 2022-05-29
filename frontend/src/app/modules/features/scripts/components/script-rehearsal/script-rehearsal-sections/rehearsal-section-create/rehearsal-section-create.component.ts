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
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import {
  IsMarkingSection,
  ScriptViewerService
} from '../../../../services/script-viewer.service';
import { SimpleScript } from '../../../../../../shared/dtos/script-dtos';
import { SimpleSection } from '../../../../../../shared/dtos/section-dtos';

@Component({
  selector: 'app-rehearsal-section-create',
  templateUrl: './rehearsal-section-create.component.html',
  styleUrls: ['./rehearsal-section-create.component.scss']
})
export class RehearsalSectionCreateComponent
  extends FormBase
  implements OnInit, OnDestroy {
  markedSection: SimpleSection = null;
  isMarkingSection: IsMarkingSection = null;
  script: SimpleScript = null;
  @Output() private cancel = new EventEmitter<void>();
  @HostBinding('class') private classes = 'd-flex flex-column flex-grow-1';
  private $destroy = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private toastService: ToastService,
    private router: Router,
    public scriptViewerService: ScriptViewerService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      sectionName: ['', [Validators.required, Validators.maxLength(100)]]
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
        this.isMarkingSection = isMarkingSection;
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
    this.router.navigateByUrl(`/scripts/${this.script?.getId()}/rehearse`);
  }
}
