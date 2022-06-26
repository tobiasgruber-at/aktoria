import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, Validators} from '@angular/forms';
import {ScriptService} from '../../../../../core/services/script/script.service';
import {ToastService} from '../../../../../core/services/toast/toast.service';
import {Theme} from '../../../../../shared/enums/theme.enum';
import {FormBase} from '../../../../../shared/classes/form-base';

@Component({
  selector: 'app-script-invite-email',
  templateUrl: './script-invite-email.component.html',
  styleUrls: ['./script-invite-email.component.scss']
})
export class ScriptInviteEmailComponent extends FormBase implements OnInit {
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

  protected processSubmit(): void {
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
