import {
  Component,
  HostBinding,
  Input,
  OnDestroy,
  OnInit
} from '@angular/core';
import { Line, Role } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-script-line',
  templateUrl: './script-line.component.html',
  styleUrls: ['./script-line.component.scss']
})
export class ScriptLineComponent implements OnInit, OnDestroy {
  @Input() line: Line;
  @Input() prevLine: Line;
  /** @see ScriptViewerService.$isEditingScript */
  isEditingScript = false;
  /** Indicates whether the user is actively interacting with this line. */
  private isInteracting = false;
  private isModalOpened = false;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private modalService: NgbModal
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
    this.line.roles = [];
  }

  /** Toggles whether this line is active. */
  toggleLineActive(): void {
    this.line.active = !this.line.active;
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  setIsActive(isActive: boolean) {
    this.isInteracting = isActive;
  }
}
