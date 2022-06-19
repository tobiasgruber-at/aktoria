import {Component, OnInit} from '@angular/core';
import {ScriptRehearsalService} from '../../services/script-rehearsal.service';
import {SimpleScript} from '../../../../shared/dtos/script-dtos';
import {SimpleSession} from '../../../../shared/dtos/session-dtos';
import {ActivatedRoute, Router} from '@angular/router';
import {ScriptService} from '../../../../core/services/script/script.service';
import {SessionService} from '../../../../core/services/session/session.service';
import {SimpleSection} from '../../../../shared/dtos/section-dtos';
import {SectionService} from '../../../../core/services/section/section.service';
import {ScriptViewerService} from '../../services/script-viewer.service';
import {FormBase} from '../../../../shared/classes/form-base';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastService} from '../../../../core/services/toast/toast.service';
import {Theme} from '../../../../shared/enums/theme.enum';
import {Subject, takeUntil} from 'rxjs';

@Component({
  selector: 'app-script-review',
  templateUrl: './script-review.component.html',
  styleUrls: ['./script-review.component.scss'],
  providers: [ScriptViewerService]
})
export class ScriptReviewComponent extends FormBase implements OnInit {
  script: SimpleScript;
  session: SimpleSession;
  section: SimpleSection;
  private $destroy = new Subject<void>();

  constructor(public scriptRehearsalService: ScriptRehearsalService,
              private route: ActivatedRoute,
              private scriptService: ScriptService,
              private sessionService: SessionService,
              private sectionService: SectionService,
              public scriptViewerService: ScriptViewerService,
              private formBuilder: FormBuilder,
              private toastService: ToastService,
              private router: Router) {
    super(toastService);
  }

  ngOnInit(): void {
    const handleError = () => {
      this.toastService.show({
        message: 'Keine aktive Lerneinheit gefunden',
        theme: Theme.danger
      });
      this.router.navigateByUrl('/scripts');
    };
    this.form = this.formBuilder.group({
      assessment: ['', [Validators.required]]
    });

    this.scriptRehearsalService.$script.pipe(takeUntil(this.$destroy)).subscribe(script => {
      this.script = script;
      this.scriptViewerService.setScript(script);
      if (script === null) {
        handleError();
      }
    });
    this.scriptRehearsalService.$session.pipe(takeUntil(this.$destroy)).subscribe(session => {
      this.session = session;
      this.scriptViewerService.setMarkedSection({section: session.getSection(), scrollTo: true});
      if (session === null) {
        handleError();
      }
    });
  }

  protected processSubmit(): void {
    const {assessment} = this.form.value;
    this.sessionService.patchOne(this.session.id, {selfAssessment: assessment}).pipe(takeUntil(this.$destroy)).subscribe({
      next: () => {
        this.router.navigateByUrl('/scripts');
        this.toastService.show({
          message: 'Lerneinheit abgeschlossen.',
          theme: Theme.primary
        });
      },
      error: (err) => this.handleError(err)
    });
  }
}
