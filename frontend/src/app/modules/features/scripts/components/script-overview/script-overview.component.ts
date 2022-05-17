import { Component, OnInit, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { DetailedScript } from '../../../../shared/dtos/script-dtos';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { Theme } from '../../../../shared/enums/theme.enum';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scriptService: ScriptService,
    private toastService: ToastService,
    private modalService: NgbModal
  ) {}

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
            this.getLoading = false;
          },
          error: handleNotFound
        });
      }
    });
  }

  openModal(modal: TemplateRef<any>) {
    this.deleteError = null;
    this.modalService.open(modal, { centered: true });
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
}
