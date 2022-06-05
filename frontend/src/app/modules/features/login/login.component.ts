import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthRequest } from '../../shared/dtos/auth-request';
import { ToastService } from '../../core/services/toast/toast.service';
import { FormBase } from '../../shared/classes/form-base';
import { AuthService } from '../../core/services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends FormBase implements OnInit {
  returnTo: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    toastService: ToastService,
    private activatedRoute: ActivatedRoute
  ) {
    super(toastService);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
    this.returnTo =
      this.activatedRoute.snapshot.queryParamMap.get('returnTo') || '/scripts';
  }

  /**
   * Send authentication data to the authService.
   * If the authentication was successfully, the user will be forwarded to the message page
   */
  protected processSubmit(): void {
    const { email, password } = this.form.value;
    console.log('Try to authenticate user: ' + email);
    this.authService.loginUser(new AuthRequest(email, password)).subscribe({
      next: () => {
        this.toggleLoading(false);
        console.log('Successfully logged in user: ' + email);
        this.router.navigateByUrl(this.returnTo);
      },
      error: (err) => this.handleError(err)
    });
  }
}
