import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { ScriptService } from '../../../../../core/services/script/script.service';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { Theme } from '../../../../../shared/enums/theme.enum';
import { FormBase } from '../../../../../shared/classes/form-base';

@Component({
  selector: 'app-script-invite-link',
  templateUrl: './script-invite-link.component.html',
  styleUrls: ['./script-invite-link.component.scss']
})
export class ScriptInviteLinkComponent extends FormBase implements OnInit {
  scriptId: string;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      link: ''
    });
    this.scriptId = this.route.snapshot.paramMap.get('id');
  }

  protected processSubmit(): void {
    this.scriptService.inviteLink(this.scriptId).subscribe({
      next: (res) => {
        this.toggleLoading(false);
        this.toastService.show({
          message: 'Einladungslink erstellt!',
          theme: Theme.primary
        });
        console.log(res);
        this.form.value.link = res;
      },
      error: (err) => this.handleError(err)
    });
  }
}
