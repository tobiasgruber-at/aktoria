import {Component, ElementRef, HostBinding, Input, OnDestroy, OnInit} from '@angular/core';
import {Line, Page} from '../../../../../shared/dtos/script-dtos';
import {ScriptViewerService} from '../../../services/script-viewer.service';
import {Subject, takeUntil} from 'rxjs';
import {SimpleSection} from '../../../../../shared/dtos/section-dtos';
import {HelpersService} from '../../../../../core/services/helpers/helpers.service';

@Component({
  selector: 'app-script-page',
  templateUrl: './script-page.component.html',
  styleUrls: ['./script-page.component.scss']
})
export class ScriptPageComponent implements OnInit, OnDestroy {
  @Input() page: Page;
  section: SimpleSection = null;
  renderedContent = false;
  private isEditing = false;
  private $destroy = new Subject<void>();

  constructor(
    private ref: ElementRef,
    private scriptViewerService: ScriptViewerService,
    private helpersService: HelpersService
  ) {
  }

  @HostBinding('class')
  get classes(): string[] {
    const classes = [];
    if (this.isEditing) {
      classes.push('is-editing');
    }
    return classes;
  }

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
    this.lazyRenderContent();
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

  /**
   * Lazy renders page content when editing.
   *
   * Only renders the page content if not editing (because line have to be rendered for example on scroll-to line),
   * or when it's near enough to the view, so that for huge scripts all the line logic does not have to be
   * calculated until it's necessary.
   */
  private lazyRenderContent(): void {
    if (!this.isEditing) {
      this.renderedContent = true;
    } else {
      this.helpersService.$mainScrollPosY
        .pipe(takeUntil(this.$destroy))
        .subscribe(() => {
          if (!this.renderedContent) {
            const pagePosY = this.ref.nativeElement.getBoundingClientRect().top;
            const windowHeight = window.innerHeight;
            if (pagePosY < windowHeight * 5) {
              this.renderedContent = true;
            }
          }
        });
    }
  }
}
