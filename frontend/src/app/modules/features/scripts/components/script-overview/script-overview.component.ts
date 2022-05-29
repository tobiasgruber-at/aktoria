import {Component, OnInit, TemplateRef} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {DetailedScript, Role} from '../../../../shared/dtos/script-dtos';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';
import {SimpleUser} from '../../../../shared/dtos/user-dtos';
import {AuthService} from '../../../../core/services/auth/auth-service';

@Component({
  selector: 'app-script-overview',
  templateUrl: './script-overview.component.html',
  styleUrls: ['./script-overview.component.scss']
})
export class ScriptOverviewComponent implements OnInit {
  getLoading = true;
  getError = null;
  deleteLoading = false;
  deleteError = null;
  script: DetailedScript = null;
  selectedRole: Role = null;
  members: SimpleUser[];
  readonly theme = Theme;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scriptService: ScriptService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      const handleNotFound = () => {
        this.getError = 'Skript konnte nicht gefunden werden.';
        this.getLoading = false;
      };
      if (isNaN(id)) {
        handleNotFound();
      } else {
        this.scriptService.getOne(id).subscribe({
          next: (script) => {
            this.script = script;
            this.members = [];
            this.members.push(script.owner);
            Array.prototype.push.apply(this.members, script.participants);
            this.selectedRole = this.script.roles[0];
            this.getLoading = false;
          },
          error: handleNotFound
        });
      }
    });
  }

  openModal(modal: TemplateRef<any>) {
    this.deleteError = null;
    this.modalService.open(modal, {centered: true});
  }

  deleteScript(modal: NgbActiveModal): void {
    this.deleteLoading = true;
    this.scriptService.delete(this.script.id).subscribe({
      next: () => {
        modal.dismiss();
        this.router.navigateByUrl('/scripts');
        this.toastService.show({
          message: 'Skript erfolgreich gelöscht.',
          theme: Theme.primary
        });
      },
      error: (err) => {
        this.deleteLoading = false;
        this.deleteError = err.error?.message;
      }
    });
  }

  exitScript(modal: NgbActiveModal): void {
    this.deleteLoading = true;
    this.scriptService
      .removeParticipant(this.script.id, this.authService.getEmail())
      .subscribe({
        next: () => {
          modal.dismiss();
          this.router.navigateByUrl('/scripts');
          this.toastService.show({
            message: 'Skript erfolgreich verlassen.',
            theme: Theme.primary
          });
        },
        error: (err) => {
          this.deleteLoading = false;
          this.deleteError = err.error?.message;
        }
      });
  }

  isOwner() {
    if (this.script) {
      return this.script.owner.email === this.authService.getEmail();
    }
    return false;
  }

  selectRole(role: Role): void {
    this.selectedRole = role;
  }
}
