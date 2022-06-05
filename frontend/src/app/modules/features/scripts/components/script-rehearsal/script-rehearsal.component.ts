import { Component, OnDestroy, OnInit } from '@angular/core';
import { ScriptRehearsalService } from '../../services/script-rehearsal.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ScriptService } from '../../../../core/services/script/script.service';
import { DetailedScript } from '../../../../shared/dtos/script-dtos';
import { SimpleSession } from '../../../../shared/dtos/session-dtos';
import { Subject, takeUntil } from 'rxjs';
import { lineAppearAnimations } from '../../animations/rehearsal-line.animations';
import { ToastService } from '../../../../core/services/toast/toast.service';
import { Theme } from '../../../../shared/enums/theme.enum';

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
  synth;
  cancel: boolean;
  paused: boolean;
  private $destroy = new Subject<void>();

  constructor(
    public scriptRehearsalService: ScriptRehearsalService,
    private route: ActivatedRoute,
    private scriptService: ScriptService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.synth = window.speechSynthesis;

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
        } else {
          this.readLine();
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
    this.cancel = true;
    this.synth.cancel();
  }

  readLine() {
    this.cancel = true;
    this.synth.cancel();

    const lines = this.session.getLines();
    let currentLine;

    for (const line of lines) {
      if (line.index === this.session.currentLineIndex) {
        currentLine = line;
        break;
      }
    }

    if (!(currentLine.roles.some((r) => r.name === this.session.role?.name))) {
      const utter = new SpeechSynthesisUtterance(currentLine.content);

      utter.addEventListener('end', () => {
        if (!this.cancel) {
          this.session.currentLineIndex++;
          this.readLine();
        }
      });

      setTimeout(() => {
        this.cancel = false;
        this.synth.speak(utter);
        if (this.paused) {
          this.synth.pause();
        }
      }, 10);
    }
  }

  pauseRead() {
    this.paused = true;
    this.synth.pause();
  }

  resumeRead() {
    this.paused = false;
    this.synth.resume();
  }
}
