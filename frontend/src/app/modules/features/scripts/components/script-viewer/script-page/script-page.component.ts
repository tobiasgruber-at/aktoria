import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Line, Page } from '../../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../../services/script-viewer.service';
import { Subject, takeUntil } from 'rxjs';
import { SimpleSection } from '../../../../../shared/dtos/section-dtos';

@Component({
  selector: 'app-script-page',
  templateUrl: './script-page.component.html',
  styleUrls: ['./script-page.component.scss']
})
export class ScriptPageComponent implements OnInit, OnDestroy {
  @Input() page: Page;
  section: SimpleSection = null;
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
    this.scriptViewerService.$markedSection
      .pipe(takeUntil(this.$destroy))
      .subscribe((markedSection) => {
        this.section = markedSection?.section;
      });
  }

  ngOnDestroy() {
    this.$destroy.next();
    this.$destroy.complete();
  }

  isNotInSection(line: Line): boolean {
    return (
      this.section &&
      (line.index < this.section.startLine || line.index > this.section.endLine)
    );
  }
}
