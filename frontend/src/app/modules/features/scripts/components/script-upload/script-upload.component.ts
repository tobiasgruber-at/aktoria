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
      file: [null, [Validators.required, Validators.maxLength(1)]],
      startPage: [0, [Validators.required, Validators.maxLength(10)]]
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
    const { file, startPage } = this.form.value;
    this.scriptService.parse(file, startPage).subscribe({
      next: (res) => {
        let script = res;
        this.toastService.show({
          message: 'Skript wurde erfolgreich hochgeladen!',
          theme: Theme.primary
        });
        this.router.navigateByUrl('/scripts');
        // TODO: refactor, so that the script is saved after a review by the user
        this.scriptService.save(res).subscribe({
          next: (detailedScript) => {
            script = detailedScript;
            console.log(script);
          }
        });
      },

      error: (err) => this.handleError(err)
    });
  }
}
