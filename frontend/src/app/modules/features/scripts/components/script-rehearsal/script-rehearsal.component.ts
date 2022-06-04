import {Component, OnDestroy, OnInit} from '@angular/core';
import {ScriptRehearsalService} from '../../services/script-rehearsal.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {SimpleSession} from '../../../../shared/dtos/session-dtos';
import {combineLatest, Subject, takeUntil} from 'rxjs';
import {lineAppearAnimations} from '../../animations/rehearsal-line.animations';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {SessionService} from '../../../../core/services/session/session.service';
import {SectionService} from '../../../../core/services/section/section.service';
import {fixedAppearAnimations} from '../../../../shared/animations/fixed-appear-animations';

/** Presents a script rehearsal. */
@Component({
  selector: 'app-script-rehearsal',
  templateUrl: './script-rehearsal.component.html',
  styleUrls: ['./script-rehearsal.component.scss'],
  animations: [lineAppearAnimations, fixedAppearAnimations]
})
export class ScriptRehearsalComponent implements OnInit, OnDestroy {
  session: SimpleSession = null;
  getLoading = true;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private route: ActivatedRoute,
    private scriptService: ScriptService,
    private sectionService: SectionService,
    private toastService: ToastService,
    private sessionService: SessionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const scriptId = +params.get('scriptId');
      const rehearsalId = +params.get('rehearsalId');
      const handleNotFound = (err) => {
        this.toastService.showError(err);
        this.router.navigateByUrl('/scripts');
      };
      combineLatest([
        this.scriptService.getOne(scriptId),
        this.sessionService.getOne(rehearsalId)
      ])
        .pipe(takeUntil(this.$destroy))
        .subscribe({
          next: ([script, session]) => {
            this.sectionService
              .getOne(session.sectionId)
              .pipe(takeUntil(this.$destroy))
              .subscribe({
                next: (section) => {
                  this.scriptRehearsalService.setScript(script);
                  this.scriptRehearsalService.setSession(session);
                  session.init(script, section);
                  this.getLoading = false;
                },
                error: handleNotFound
              });
          },
          error: handleNotFound
        });
    });
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }
}
