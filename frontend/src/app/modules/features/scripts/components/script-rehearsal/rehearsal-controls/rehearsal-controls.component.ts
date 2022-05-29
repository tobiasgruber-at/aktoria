import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {ScriptRehearsalService} from '../../../services/script-rehearsal.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSession} from '../../../../../shared/dtos/session-dtos';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';
import {DetailedScript} from '../../../../../shared/dtos/script-dtos';

@Component({
  selector: 'app-rehearsal-controls',
  templateUrl: './rehearsal-controls.component.html',
  styleUrls: ['./rehearsal-controls.component.scss']
})
export class RehearsalControlsComponent implements OnInit, OnDestroy {
  script: DetailedScript = null;
  session: SimpleSession = null;
  interactionDisabled = false;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private modalService: NgbModal,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
    this.scriptRehearsalService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  changeLine(line: 'next' | 'prev'): void {
    if (this.interactionDisabled) {
      return;
    }
    this.interactionDisabled = true;
    if (line === 'next' && !this.session.isAtEnd()) {
      this.session.currentLine++;
    } else if (line === 'prev' && !this.session.isAtStart()) {
      this.session.currentLine--;
    }
    this.scriptRehearsalService.setSession(this.session);
    setTimeout(() => {
      this.interactionDisabled = false;
    }, 300);
  }

  openModal(modal: TemplateRef<any>): void {
    this.modalService.open(modal, {centered: true});
  }

  stopSession(modal: NgbActiveModal): void {
    modal.dismiss();
    this.router.navigateByUrl(`/scripts/${this.script.id}`);
  }
}
