import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';
import {FormBase} from '../../../../shared/classes/form-base';
import {ScriptService} from '../../../../core/services/script/script.service';

@Component({
  selector: 'app-script-invite',
  templateUrl: './script-invite.component.html',
  styleUrls: ['./script-invite.component.scss']
})
export class ScriptInviteComponent extends FormBase implements OnInit {

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
      email: ['', [Validators.required, Validators.email]]
    });
    this.scriptId = this.route.snapshot.paramMap.get('id');
  }

  protected sendSubmit(): void {
    const {email} = this.form.value;
    this.scriptService.inviteParticipant(email, this.scriptId).subscribe({
      next: () => {
        this.toggleLoading(false);
        this.toastService.show({
          message: 'Einladung wurde gesendet!',
          theme: Theme.primary
        });
      },
      error: (err) => this.handleError(err)
    });
  }

}
