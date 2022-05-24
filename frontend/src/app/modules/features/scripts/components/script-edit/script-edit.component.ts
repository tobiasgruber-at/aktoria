import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ScriptService } from '../../../../core/services/script/script.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { Theme } from '../../../../shared/enums/theme.enum';
import { FormBase } from '../../../../shared/classes/form-base';
import { FormBuilder, Validators } from '@angular/forms';
import { ScriptViewerService } from '../../services/script-viewer.service';
import { SimpleScript } from '../../../../shared/dtos/script-dtos';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-edit-review',
  templateUrl: './script-edit.component.html',
  styleUrls: ['./script-edit.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptEditComponent
  extends FormBase
  implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('tutorialModal') tutorialModal: ElementRef;
  getLoading = true;
  script: SimpleScript = null;
  isUploading = false;
  private $destroy = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService,
    public scriptViewerService: ScriptViewerService,
    private modalService: NgbModal,
    private route: ActivatedRoute
  ) {
    super(toastService);
    this.scriptViewerService.setIsEditing(true);
    this.isUploading = route.snapshot.data?.isUploading || false;
    this.scriptViewerService.setIsUploading(this.isUploading);
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      scriptName: ['', [Validators.required, Validators.maxLength(100)]]
    });
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
    if (this.isUploading) {
      this.handleUploading();
    } else {
      this.handleNotUploading();
    }
  }

  openModal(modalRef): void {
    this.modalService.open(modalRef, { centered: true });
  }

  cancelUpload(modal: NgbActiveModal): void {
    this.router.navigateByUrl('/scripts');
    modal.dismiss();
    this.toastService.show({
      message: 'Skript Upload abgebrochen.',
      theme: Theme.primary
    });
  }

  ngAfterViewInit() {
    if (this.isUploading) {
      this.openModal(this.tutorialModal);
    }
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  protected processSubmit(): void {
    const { scriptName } = this.form.value;
    const script = {
      ...this.script,
      name: scriptName
    };
    this.scriptService.save(script).subscribe({
      next: (detailedScript) => {
        this.router.navigateByUrl(`/scripts/${detailedScript.id}`);
        this.toastService.show({
          message: 'Skript wurde erfolgreich hochgeladen!',
          theme: Theme.primary
        });
      },
      error: (err) => this.handleError(err)
    });
  }

  /** Fetches staged script that should be reviewed. */
  private handleUploading() {
    const handleNoStagedScript = () => {
      this.router.navigateByUrl('/scripts');
      this.toastService.show({
        message: 'Es wird derzeit kein Skript erstellt.',
        theme: Theme.danger
      });
      this.getLoading = false;
    };
    this.scriptService.$stagedScript.pipe(takeUntil(this.$destroy)).subscribe({
      next: (script) => {
        if (!script) {
          handleNoStagedScript();
        }
        this.scriptViewerService.setScript(script);
        this.form.patchValue({
          scriptName: script.name
        });
        this.getLoading = false;
      },
      error: handleNoStagedScript
    });
  }

  /** Fetches existing script that should be edited. */
  private handleNotUploading() {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      const handleNotFound = () => {
        // TODO: show error
        //this.getError = 'Skript konnte nicht gefunden werden.';
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
}
