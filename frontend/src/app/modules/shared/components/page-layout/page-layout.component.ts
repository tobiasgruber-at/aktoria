import {Component, Input} from '@angular/core';
import {Router} from '@angular/router';
import {Theme} from '../../enums/theme.enum';
import {ToastService} from '../../../core/services/toast/toast.service';
import {AuthService} from '../../../core/services/auth/auth.service';
import {fixedAppearAnimations} from '../../animations/fixed-appear-animations';
import {appearAnimations} from '../../animations/appear-animations';
import {HelpersService} from '../../../core/services/helpers/helpers.service';

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
export class PageLayoutComponent {
  @Input() showHeader = true;
  @Input() showFooter = false;
  @Input() showSidebar = false;
  @Input() theme: 'light' | 'dark' = 'light';
  @Input() loading = false;

  constructor(
    public authService: AuthService,
    private router: Router,
    private toastService: ToastService,
    private helpersService: HelpersService
  ) {}

  logout(): void {
    this.router.navigateByUrl('/login');
    this.toastService.show({
      message: 'Erfolgreich ausgeloggt!',
      theme: Theme.primary
    });
    this.authService.logoutUser();
  }

  onScrollMain(e): void {
    this.helpersService.setMainScrollPosY(e.target.scrollTop);
  }
}
