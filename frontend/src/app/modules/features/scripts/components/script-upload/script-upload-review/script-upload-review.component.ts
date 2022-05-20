import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { Router } from '@angular/router';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { Theme } from '../../../../../shared/enums/theme.enum';
import { FormBase } from '../../../../../shared/classes/form-base';
import { FormBuilder, Validators } from '@angular/forms';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { SimpleScript } from '../../../../../shared/dtos/script-dtos';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-script-upload-review',
  templateUrl: './script-upload-review.component.html',
  styleUrls: ['./script-upload-review.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptUploadReviewComponent
  extends FormBase
  implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('tutorialModal') tutorialModal: ElementRef;
  getLoading = true;
  script: SimpleScript = null;
  private $destroy = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService,
    public scriptViewerService: ScriptViewerService,
    private modalService: NgbModal
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      scriptName: ['', [Validators.required, Validators.maxLength(100)]]
    });
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
    const handleNoStagedScript = () => {
      this.router.navigateByUrl('/scripts');
      this.toastService.show({
        message: 'Es wird derzeit kein Skript erstellt.',
        theme: Theme.danger
      });
      this.getLoading = false;
    };
    this.scriptService.$stagedScript.pipe(takeUntil(this.$destroy)).subscribe({
      next: (script) => {
        if (!script) {
          handleNoStagedScript();
        }
        this.getLoading = false;
        this.scriptViewerService.setScript(script);
        this.scriptViewerService.setIsEditing(true);
        this.form.patchValue({
          scriptName: script.name
        });
      },
      error: handleNoStagedScript
    });
  }

  ngAfterViewInit() {
    this.modalService.open(this.tutorialModal, { centered: true });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  protected sendSubmit(): void {
    const { scriptName } = this.form.value;
    const script = {
      ...this.script,
      name: scriptName
    };
    this.scriptService.save(script).subscribe({
      next: (detailedScript) => {
        this.router.navigateByUrl(`/scripts/${detailedScript.id}`);
        this.toastService.show({
          message: 'Skript wurde erfolgreich hochgeladen!',
          theme: Theme.primary
        });
      },
      error: (err) => this.handleError(err)
    });
  }
}
