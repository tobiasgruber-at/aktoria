import {Component, OnInit} from '@angular/core';
import {FormBase} from '../../../../shared/classes/form-base';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {FormBuilder} from '@angular/forms';
import {UserService} from '../../../../core/services/user/user.service';
import {Theme} from '../../../../shared/enums/theme.enum';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.scss']
})
export class EmailVerificationComponent extends FormBase implements OnInit {
  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({});
  }

  protected processSubmit(): void {
    this.userService.resendVerificationEmail().subscribe({
      next: () => {
        this.toastService.show({
          message: 'Email wurde versendet.',
          theme: Theme.primary
        });
        this.toggleLoading(false);
      },
      error: (err) => this.handleError(err)
    });
  }
}
