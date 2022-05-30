import {
  Component,
  ElementRef,
  HostBinding,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Line, Role } from '../../../../../shared/dtos/script-dtos';
import {
  IsMarkingSection,
  ScriptViewerService
} from '../../../services/script-viewer.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject, takeUntil } from 'rxjs';
import { LineService } from '../../../../../core/services/line/line.service';
import { ToastService } from '../../../../../core/services/toast/toast.service';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';
import { appearAnimations } from '../../../../../shared/animations/appear-animations';

@Component({
  selector: 'app-script-line',
  templateUrl: './script-line.component.html',
  styleUrls: ['./script-line.component.scss'],
  animations: [appearAnimations]
})
export class ScriptLineComponent implements OnInit, OnDestroy {
  @ViewChild('scrollAnchor') scrollAnchorRef: ElementRef;
  @Input() line: Line;
  @Input() prevLine: Line;
  /** @see ScriptViewerService.$isEditingScript */
  isEditingScript = false;
  section: SimpleSection = null;
  /** Indicates whether the user is actively interacting with this line. */
  private isInteracting = false;
  private isUploading = false;
  private isModalOpened = false;
  private isMarkingSection: IsMarkingSection = null;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private modalService: NgbModal,
    private lineService: LineService,
    private toastService: ToastService
  ) {}

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isEditingScript) {
      classes.push('is-editing-script');
    }
    if (!this.line.active) {
      classes.push('is-hidden');
    }
    if (this.isInteracting || this.isModalOpened) {
      classes.push('is-interacting');
    }
    if (this.isMarkingSection) {
      classes.push('is-marking-section');
    }
    return classes;
  }

  /** @return Whether this line is an instruction, or a spoken line. */
  get isInstruction() {
    return !this.line.roles || this.line.roles?.length < 1;
  }

  /** @return Whether this line has exactly the same authors assigned as the prev line. */
  get sameRolesAsPrevLine(): boolean {
    return (
      this.prevLine?.roles &&
      this.line.roles?.length > 0 &&
      this.line.roles.length === this.prevLine.roles.length &&
      this.line.roles.every((a) =>
        this.prevLine.roles.some((b) => b.name === a.name)
      )
    );
  }

  ngOnInit() {
    if (this.line.roles === null) {
      this.line.roles = [];
    }
    this.scriptViewerService.$isEditingScript
      .pipe(takeUntil(this.$destroy))
      .subscribe((isEditing) => {
        this.isEditingScript = isEditing;
      });
    this.scriptViewerService.$isUploading
      .pipe(takeUntil(this.$destroy))
      .subscribe((isUploading) => {
        this.isUploading = isUploading;
      });
    this.scriptViewerService.$isMarkingSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((isMarkingSection) => {
        this.isMarkingSection = isMarkingSection;
      });
    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((markedSection) => {
        this.section = markedSection?.section;
        if (
          this.section &&
          this.scrollAnchorRef &&
          markedSection.scrollTo &&
          this.line.index === this.section.startLine.index
        ) {
          this.scrollAnchorRef.nativeElement.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
          });
        }
      });
  }

  @HostListener('mouseenter')
  updateMarkedSection(): void {
    if (this.isMarkingSection) {
      const updatedSection: SimpleSection = this.section;
      if (this.isMarkingSection === 'start') {
        updatedSection.startLine = this.line;
      } else if (
        this.isMarkingSection === 'end' &&
        this.line.index >= this.section.startLine.index
      ) {
        updatedSection.endLine = this.line;
      }
      this.scriptViewerService.setMarkedSection({
        section: updatedSection,
        scrollTo: false
      });
    }
  }

  @HostListener('click')
  fixMarkedSection(): void {
    if (this.isMarkingSection) {
      if (this.isMarkingSection === 'start') {
        this.scriptViewerService.setIsMarkingSection('end');
        this.section.endLine = this.section.startLine;
        this.scriptViewerService.setMarkedSection({
          section: this.section,
          scrollTo: false
        });
      } else if (this.isMarkingSection === 'end') {
        this.scriptViewerService.setIsMarkingSection(null);
      }
    }
  }

  /** @return Whether a line is highlighted. */
  isHighlighted(selectedRole: Role): boolean {
    return this.line.roles?.some((r) => r.name === selectedRole?.name);
  }

  toggleModal(modal): void {
    this.isModalOpened = true;
    const modalRef = this.modalService.open(modal, { centered: true });
    modalRef.result.finally(() => {
      this.isModalOpened = false;
    });
  }

  /** Clears all roles of this line. */
  removeRoles(): void {
    if (this.isUploading) {
      this.line.roles = [];
    } else {
      this.scriptViewerService.setLoading(true);
      this.lineService.patchLine({ roleIds: [] }, this.line.id).subscribe({
        next: (line) => {
          this.scriptViewerService.setLoading(false);
          this.line.roles = [];
        },
        error: (err) => {
          this.scriptViewerService.setLoading(false);
          this.toastService.showError(err);
        }
      });
    }
  }

  /** Toggles whether this line is active. */
  toggleLineActive(): void {
    if (this.isUploading) {
      this.line.active = !this.line.active;
    } else {
      this.scriptViewerService.setLoading(true);
      this.lineService
        .patchLine({ active: !this.line.active }, this.line.id)
        .subscribe({
          next: (line) => {
            this.scriptViewerService.setLoading(false);
            this.line.active = line.active;
          },
          error: (err) => {
            this.scriptViewerService.setLoading(false);
            this.toastService.showError(err);
          }
        });
    }
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  setIsActive(isActive: boolean) {
    this.isInteracting = isActive;
  }
}
