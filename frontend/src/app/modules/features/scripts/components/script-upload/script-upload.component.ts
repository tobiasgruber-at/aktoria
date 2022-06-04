import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {FormBase} from '../../../../shared/classes/form-base';
import {Router} from '@angular/router';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {ScriptService} from '../../../../core/services/script/script.service';
import {appearAnimations} from '../../../../shared/animations/appear-animations';

/** Script upload form. */
@Component({
  selector: 'app-script-upload',
  templateUrl: './script-upload.component.html',
  styleUrls: ['./script-upload.component.scss'],
  animations: [appearAnimations]
})
export class ScriptUploadComponent extends FormBase implements OnInit {
  isCollapsed = true;

  constructor(
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      file: [null, [Validators.required, Validators.maxLength(1)]],
      startPage: [
        0,
        [Validators.required, Validators.min(0), Validators.maxLength(10)]
      ]
    });
  }

  onFileChanged(file: File) {
    this.form.patchValue({ file });
  }

  onFileRemoved(fileInputNode) {
    this.onFileChanged(null);
    fileInputNode.value = null;
  }

  protected processSubmit() {
    const { file, startPage } = this.form.value;
    this.scriptService.parse(file, startPage).subscribe({
      next: (script) => {
        this.scriptService.setStagedScript(script);
        this.router.navigateByUrl('/scripts/upload/review');
      },
      error: (err) => this.handleError(err)
    });
  }
}
