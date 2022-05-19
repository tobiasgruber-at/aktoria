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
  isEditing = false;
  isActive = false;
  isModalOpened = false;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private modalService: NgbModal
  ) {}

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isInstruction) {
      classes.push('py-2');
    }
    if (this.isEditing) {
      classes.push('is-editing');
    }
    if (this.isActive || this.isModalOpened) {
      classes.push('is-active');
    }
    return classes;
  }

  /** @return Whether this line is an instruction, or a spoken line. */
  get isInstruction() {
    return !this.line?.roles || this.line?.roles?.length < 1;
  }

  ngOnInit() {
    if (this.line.roles === null) {
      this.line.roles = [];
    }
    this.scriptViewerService.$isEditing
      .pipe(takeUntil(this.$destroy))
      .subscribe((isEditing) => {
        this.isEditing = isEditing;
      });
  }

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

  changeRoles(toggledRole: Role): void {
    if (this.line.roles.some((r) => r.name === toggledRole.name)) {
      this.line.roles = this.line.roles.filter(
        (r) => r.name !== toggledRole.name
      );
      return;
    }
    this.line.roles.push(toggledRole);
  }

  removeRoles(): void {
    this.line.roles = [];
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  setIsActive(isActive: boolean) {
    this.isActive = isActive;
  }
}
