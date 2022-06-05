import {Component, OnDestroy, OnInit} from '@angular/core';
import {ScriptViewerService} from '../../../services/script-viewer.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleScript} from '../../../../../shared/dtos/script-dtos';
import {Router} from '@angular/router';

/** Control panel for script reading. */
@Component({
  selector: 'app-script-read-controls',
  templateUrl: './script-read-controls.component.html',
  styleUrls: ['./script-read-controls.component.scss']
})
export class ScriptReadControlsComponent implements OnInit, OnDestroy {
  script: SimpleScript = null;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
  }

  close() {
    this.router.navigateByUrl(`/scripts/${this.script.getId()}`);
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }
}
