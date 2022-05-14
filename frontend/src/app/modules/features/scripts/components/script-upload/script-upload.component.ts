import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { FormBase } from '../../../../shared/classes/form-base';
import { Router } from '@angular/router';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { ScriptService } from '../../../../core/services/script/script.service';
import { Theme } from '../../../../shared/enums/theme.enum';
import { appearAnimations } from '../../../../shared/animations/appear-animations';

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
      file: [null, [Validators.required, Validators.maxLength(1)]]
    });
  }

  onFileChanged(file: File) {
    this.form.patchValue({ file });
  }

  onFileRemoved(fileInputNode) {
    this.onFileChanged(null);
    fileInputNode.value = null;
  }

  protected sendSubmit() {
    this.scriptService.post(this.form.value).subscribe({
      next: (res) => {
        const script = res;
        this.toastService.show({
          message: 'Datei wurde erfolgreich hochgeladen!',
          theme: Theme.primary
        });
        this.router.navigateByUrl('/scripts');

        // TODO: refactor
        /*
        this.scriptService.postCorrected(res).subscribe({
          next: (detailedScript) => {
            script = detailedScript;
            console.log(script);
            //TODO: remove console log
          }hallihallo
        });
         */
      },

      error: (err) => this.handleError(err)
    });
  }
}
