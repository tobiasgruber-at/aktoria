import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Line, SimpleScript} from '../../../../shared/dtos/script-dtos';
import {ScriptViewerService} from '../../services/script-viewer.service';
import {Subject, takeUntil} from 'rxjs';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';

@Component({
  selector: 'app-script-conflicts',
  templateUrl: './script-conflicts.component.html',
  styleUrls: ['./script-conflicts.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: ScriptConflictsComponent
    }
  ]
})
export class ScriptConflictsComponent implements OnInit, OnDestroy, ControlValueAccessor {
  @Input() type: 'error' | 'warning' = 'error';
  @Output() countChange = new EventEmitter<number>();
  script: SimpleScript = null;
  lines: Line[];
  clickCounter = 0;
  touched = false;
  isDisabled = false;
  private $destroy = new Subject<void>();

  constructor(private scriptViewerService: ScriptViewerService) {
  }

  ngOnInit(): void {
    this.scriptViewerService.$script
      .pipe(takeUntil(this.$destroy))
      .subscribe((script) => {
        if (!this.isDisabled) {
          this.script = script;
          this.updateCount();
        }
        this.markAsTouched();
      });

    this.scriptViewerService.$resolveConflict
      .pipe(takeUntil(this.$destroy))
      .subscribe((index) => {
        if (!this.isDisabled) {
          this.lines = this.lines.filter((l) => l.index !== index);
          this.onChange(this.lines.length);
          this.countChange.emit(this.lines.length);
        }
        this.markAsTouched();
      });
  }

  ngOnDestroy(): void {
    this.$destroy.next();
    this.$destroy.complete();
  }

  onChange = (count) => {
  };

  onTouched = () => {
  };

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.touched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
    this.markAsTouched();
  }

  writeValue(lines: Line[]): void {
    this.lines = lines;
    this.updateCount();
    this.markAsTouched();
  }

  markAsTouched(): void {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }

  updateCount(): void {
    this.lines = [];
    if (this.script) {
      for (const page of this.script.pages) {
        for (const line of page.lines) {
          if (line.conflictType) {
            if (
              this.type === 'error' &&
              line.conflictType === 'ASSIGNMENT_REQUIRED'
            ) {
              this.lines.push(line);
            }
            if (
              this.type === 'warning' &&
              line.conflictType === 'VERIFICATION_REQUIRED'
            ) {
              this.lines.push(line);
            }
          }
        }
      }
    }
    this.onChange(this.lines.length);
    this.countChange.emit(this.lines.length);
  }

  jump(): void {
    this.scriptViewerService.scrollToLine(
      this.lines[this.clickCounter++ % this.lines.length]?.index
    );
    this.markAsTouched();
  }
}
