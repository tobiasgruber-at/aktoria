import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {SimpleSection} from '../../../../../shared/dtos/section-dtos';
import {SectionService} from '../../../../../core/services/section/section.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SimpleScript} from '../../../../../shared/dtos/script-dtos';
import {ScriptService} from '../../../../../core/services/script/script.service';
import {RoleService} from '../../../../../core/services/role/role.service';

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

  constructor(
    private sectionService: SectionService,
    private roleService: RoleService,
    private scriptService: ScriptService,
    private modalService: NgbModal
  ) {
  }

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
            console.log(err);
          }
        });
      },
      error: (err) => {
        console.log(err);
      }
    });
    this.startDate =
      this.session.start.toString().substring(8, 10) +
      '.' +
      this.session.start.toString().substring(5, 7) +
      '.' +
      this.session.start.toString().substring(0, 4);
  }

  openModal(modal: TemplateRef<any>) {
    this.modalService.open(modal, {centered: true});
  }
}
