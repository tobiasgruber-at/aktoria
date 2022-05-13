import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {AuthService} from '../../../core/services/auth/auth-service';
import {Router} from '@angular/router';
import {ToastService} from '../../../core/services/toast/toast.service';
import {FormBase} from '../../../shared/classes/form-base';
import {matchingPasswordsValidator} from '../../../shared/validators/matching-passwords-validator';

@Component({
  selector: 'app-restore-password',
  templateUrl: './restore-password.component.html',
  styleUrls: ['./restore-password.component.scss']
})
export class RestorePasswordComponent extends FormBase implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    toastService: ToastService
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
        password: ['', [Validators.required, Validators.minLength(8)]],
        passwordConfirm: ['', [Validators.required]]
      },
      { validators: [matchingPasswordsValidator] }
    );
  }

  protected sendSubmit(): void {
  }

}
