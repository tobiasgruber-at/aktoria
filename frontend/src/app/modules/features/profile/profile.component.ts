import { Component, OnDestroy, OnInit, TemplateRef } from '@angular/core';
import { UserService } from '../../core/services/user/user-service';
import { SimpleUser } from '../../shared/dtos/user-dtos';
import { AuthService } from '../../core/services/auth/auth-service';
import { appearAnimations } from '../../shared/animations/appear-animations';
import { ActivatedRoute, Router } from '@angular/router';
import { Theme } from '../../shared/enums/theme.enum';
import { ToastService } from '../../core/services/toast/toast.service';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  animations: [appearAnimations]
})
export class ProfileComponent implements OnInit, OnDestroy {
  user: SimpleUser;
  deleteLoading = false;
  deleteError = null;
  readonly theme = Theme;
  private $destroy = new Subject<void>();

  constructor(
    private userService: UserService,
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.userService
      .$ownUser()
      .pipe(takeUntil(this.$destroy))
      .subscribe((user) => {
        this.user = user;
      });
  }

  openDeleteModal(modal: TemplateRef<any>) {
    this.deleteError = null;
    this.modalService.open(modal, { centered: true });
  }

  deleteUser(modal: NgbActiveModal): void {
    this.deleteLoading = true;
    this.userService.delete(this.user.id).subscribe({
      next: (res) => {
        modal.dismiss();
        this.toastService.show({
          message: 'Konto erfolgreich gelöscht.',
          theme: Theme.primary
        });
        this.authService.logoutUser();
        this.router.navigateByUrl('/login');
      },
      error: (err) => {
        this.deleteLoading = false;
        this.deleteError = err.error?.message;
      }
    });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }
}
