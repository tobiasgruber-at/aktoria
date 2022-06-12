import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {SimpleSection} from '../../../../../shared/dtos/section-dtos';
import {SectionService} from '../../../../../core/services/section/section.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SimpleScript} from '../../../../../shared/dtos/script-dtos';
import {ScriptService} from '../../../../../core/services/script/script.service';
import {RoleService} from '../../../../../core/services/role/role.service';
import {Router} from '@angular/router';
import {ToastService} from '../../../../../core/services/toast/toast.service';

@Component({
  selector: 'app-session-list-item',
  templateUrl: './session-list-item.component.html',
  styleUrls: ['./session-list-item.component.scss']
})
export class SessionListItemComponent implements OnInit {
  @Input() session: SimpleSession;
  section: SimpleSection;
  script: SimpleScript;
  startDate: string;
  assessment: string;

  constructor(
    private sectionService: SectionService,
    private roleService: RoleService,
    private scriptService: ScriptService,
    private router: Router,
    private toastService: ToastService,
    private modalService: NgbModal
  ) {}

  get startLinePercentage(): number {
    return this.script && this.section
      ? (this.section.startLine.index / this.script.getLastLineIdx()) * 100
      : 0;
  }

  get endLinePercentage(): number {
    return this.script && this.section
      ? (this.section.endLine.index / this.script?.getLastLineIdx()) * 100
      : 0;
  }

  ngOnInit(): void {
    this.sectionService.getOne(this.session.sectionId).subscribe({
      next: (s) => {
        this.section = s;
        this.scriptService.getScriptBySession(this.session.id).subscribe({
          next: (sc) => {
            this.script = sc;
          },
          error: (err) => {
            this.toastService.showError(err);
          }
        });
      },
      error: (err) => {
        this.toastService.showError(err);
      }
    });
    this.startDate =
      this.session.start.toString().substring(8, 10) +
      '.' +
      this.session.start.toString().substring(5, 7) +
      '.' +
      this.session.start.toString().substring(0, 4);
    if (this.session.selfAssessment) {
      const tmp = this.session.selfAssessment.toString();
      if (tmp === 'VERY GOOD') {
        this.assessment = 'sehr gut';
      } else if (tmp === 'GOOD') {
        this.assessment = 'gut';
      } else if (tmp === 'NEEDS WORK') {
        this.assessment = 'unsicher';
      } else if (tmp === 'POOR') {
        this.assessment = 'schlecht';
      }
    }
  }

  openModal(modal: TemplateRef<any>) {
    this.modalService.open(modal, { centered: true });
  }

  continueSession() {
    this.modalService.dismissAll();
    this.router.navigateByUrl(`scripts/${this.script.getId()}/rehearse/${this.session.id}`);
  }
}
