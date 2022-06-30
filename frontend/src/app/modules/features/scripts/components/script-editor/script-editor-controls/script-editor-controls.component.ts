import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {SimpleScript} from '../../../../../shared/dtos/script-dtos';
import {Subject, takeUntil} from 'rxjs';
import {ScriptViewerService} from '../../../services/script-viewer.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-script-editor-controls',
  templateUrl: './script-editor-controls.component.html',
  styleUrls: ['./script-editor-controls.component.scss']
})
export class ScriptEditorControlsComponent implements OnInit, OnDestroy {
  @Output() showSidebarEventEmitter = new EventEmitter<boolean>();
  script: SimpleScript = null;
  private $destroy = new Subject<void>();

  constructor(
    public scriptViewerService: ScriptViewerService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        this.script = script;
      });
  }

  close() {
    this.showSidebarEventEmitter.emit();
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

}
