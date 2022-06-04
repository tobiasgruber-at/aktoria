import {Component, OnDestroy, OnInit} from '@angular/core';
import {ScriptRehearsalService} from '../../services/script-rehearsal.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {DetailedScript} from '../../../../shared/dtos/script-dtos';
import {SimpleSession} from '../../../../shared/dtos/session-dtos';
import {Subject, takeUntil} from 'rxjs';
import {lineAppearAnimations} from '../../animations/rehearsal-line.animations';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';

/** Presents a script rehearsal. */
@Component({
  selector: 'app-script-rehearsal',
  templateUrl: './script-rehearsal.component.html',
  styleUrls: ['./script-rehearsal.component.scss'],
  animations: [lineAppearAnimations]
})
export class ScriptRehearsalComponent implements OnInit, OnDestroy {
  session: SimpleSession = null;
  script: DetailedScript = null;
  getLoading = true;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private route: ActivatedRoute,
    private scriptService: ScriptService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = +params.get('id');
      const handleNotFound = (err) => {
        this.toastService.showError(err);
        this.router.navigateByUrl('/scripts');
      };
      this.scriptService.getOne(id).subscribe({
        next: (script) => {
          this.scriptRehearsalService.setScript(script);
          this.getLoading = false;
        },
        error: (e) => handleNotFound(e)
      });
    });
    this.scriptRehearsalService.$session
      .pipe(takeUntil(this.$destroy))
      .subscribe((session) => {
        this.session = session;
        if (!this.session) {
          this.router.navigateByUrl('/scripts');
          this.toastService.show({
            message: 'Übungseinheit wurde unterbrochen.',
            theme: Theme.danger
          });
        }
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
}
