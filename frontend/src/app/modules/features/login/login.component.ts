import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AuthRequest } from '../../shared/dtos/auth-request';
import { ToastService } from '../../core/services/toast.service';
import { FormBase } from '../../shared/classes/form-base';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends FormBase implements OnInit {
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
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Send authentication data to the authService.
   * If the authentication was successfully, the user will be forwarded to the message page
   */
  protected sendSubmit(): void {
    const { email, password } = this.form.value;
    console.log('Try to authenticate user: ' + email);
    this.authService.loginUser(new AuthRequest(email, password)).subscribe({
      next: () => {
        this.toggleLoading(false);
        console.log('Successfully logged in user: ' + email);
        this.router.navigateByUrl('/');
      },
      error: (err) => this.handleError(err)
    });
  }
}
