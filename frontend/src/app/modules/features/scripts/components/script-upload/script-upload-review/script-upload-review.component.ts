import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { Router } from '@angular/router';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { Theme } from '../../../../../shared/enums/theme.enum';
import { FormBase } from '../../../../../shared/classes/form-base';
import { FormBuilder, Validators } from '@angular/forms';
import { ScriptViewerService } from '../../../services/script-viewer.service';

@Component({
  selector: 'app-script-upload-review',
  templateUrl: './script-upload-review.component.html',
  styleUrls: ['./script-upload-review.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptUploadReviewComponent
  extends FormBase
  implements OnInit, OnDestroy {
  getLoading = true;
  private $destroy = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService,
    public scriptViewerService: ScriptViewerService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      scriptName: ['', [Validators.required, Validators.maxLength(100)]]
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

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  protected sendSubmit(): void {}
}
