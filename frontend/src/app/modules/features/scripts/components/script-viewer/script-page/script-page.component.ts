import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Page } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-script-page',
  templateUrl: './script-page.component.html',
  styleUrls: ['./script-page.component.scss']
})
export class ScriptPageComponent implements OnInit, OnDestroy {
  @Input() page: Page;
  private isEditing = false;
  private $destroy = new Subject<void>();

  constructor(private scriptViewerService: ScriptViewerService) {}

  get showPage(): boolean {
    return this.page.lines.some((l) => l.active) || this.isEditing;
  }

  ngOnInit() {
    this.scriptViewerService.$isEditingScript
      .pipe(takeUntil(this.$destroy))
      .subscribe((isEditing) => {
        this.isEditing = isEditing;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }
}
