import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { FormBase } from '../../../../shared/classes/form-base';
import { Router } from '@angular/router';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { ScriptService } from '../../../../core/services/script/script.service';
import { Theme } from '../../../../shared/enums/theme.enum';
import { SimpleScript } from '../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-script-upload',
  templateUrl: './script-upload.component.html',
  styleUrls: ['./script-upload.component.scss']
})
export class ScriptUploadComponent extends FormBase implements OnInit {
  fileName: string;
  script: SimpleScript;

  constructor(
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({ script: [Validators.required] });
  }

  onFileSelected(event) {
    const file: File = event.target.files[0];
    if (file) {
      this.fileName = file.name;
      const formData = new FormData();
      formData.append('thumbnail', file);

      this.scriptService.post(file).subscribe({
        next: (res) => {
          this.script = res;
          this.router.navigateByUrl('/scripts');
          this.toastService.show({
            message: 'Skript erfolgreich hochgeladen!',
            theme: Theme.primary
          });
          this.scriptService.postCorrected(res).subscribe({
            next: (detailedScript) => {
              this.script = detailedScript;
              console.log(this.script);
              //TODO: remove console log
            }
          });
        },

        error: (err) => this.handleError(err)
      });
    }
  }

  protected sendSubmit() {
    console.log('submitted');
  }
}
