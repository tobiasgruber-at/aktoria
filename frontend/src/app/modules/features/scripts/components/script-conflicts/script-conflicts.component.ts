import { Component, Input, OnInit } from '@angular/core';
import { Line, SimpleScript } from '../../../../shared/dtos/script-dtos';
import { ScriptViewerService } from '../../services/script-viewer.service';

@Component({
  selector: 'app-script-conflicts',
  templateUrl: './script-conflicts.component.html',
  styleUrls: ['./script-conflicts.component.scss']
})
export class ScriptConflictsComponent implements OnInit {
  @Input() type: 'error' | 'warning' = 'error';
  @Input() script: SimpleScript;
  lines: Line[];
  clickCounter = 0;

  constructor(private scriptViewerService: ScriptViewerService) {}

  ngOnInit(): void {
    this.updateCount();
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
  }

  jump(): void {
    const index = this.lines[this.clickCounter++ % this.lines.length]?.index;
    debugger;
    this.scriptViewerService.scrollToLine(index);
  }
}
