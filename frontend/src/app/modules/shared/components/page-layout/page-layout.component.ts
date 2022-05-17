import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Theme} from '../../enums/theme.enum';
import {ToastService} from '../../../core/services/toast/toast.service';
import {AuthService} from '../../../core/services/auth/auth-service';
import {fixedAppearAnimations} from '../../animations/fixed-appear-animations';
import {appearAnimations} from '../../animations/appear-animations';

/**
 * Common page layout, including header and footer.
 *
 * @author Tobias Gruber
 */
@Component({
  selector: 'app-page-layout',
  templateUrl: './page-layout.component.html',
  styleUrls: ['./page-layout.component.scss'],
  animations: [fixedAppearAnimations, appearAnimations]
})
export class PageLayoutComponent implements OnInit {
  @Input() showHeader = true;
  @Input() showFooter = false;
  @Input() theme: 'light' | 'dark' = 'light';
  @Input() loading = false;

  constructor(
    public authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {}

  logout(): void {
    this.router.navigateByUrl('/login');
    this.toastService.show({
      message: 'Erfolgreich ausgeloggt!',
      theme: Theme.primary
    });
    this.authService.logoutUser();
  }
}
