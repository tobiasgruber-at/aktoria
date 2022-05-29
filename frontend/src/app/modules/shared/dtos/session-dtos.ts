import { Line, Role, SimpleScript } from './script-dtos';
import { SimpleSection } from './section-dtos';

export class SimpleSession {
  private lines: Line[];

  constructor(
    public id: number,
    public start: Date,
    public end: Date,
    //public selfAssessment: AssessmentType,
    //public deprecated: boolean,
    //public coverage: number,
    public section: SimpleSection,
    public currentLine: number,
    public role: Role
  ) {}

  getLines(script: SimpleScript): Line[] {
    if (!this.lines && script) {
      this.lines = [];
      pagesLoop: for (const page of script?.pages) {
        for (const line of page.lines) {
          if (line.index > this.section.endLine.index) {
            break pagesLoop;
          }
          if (line.index >= this.section.startLine.index && line.active) {
            this.lines.push(line);
          }
        }
      }
    }
    return this.lines || [];
  }

  isAtStart(): boolean {
    return this.currentLine === this.section.startLine.index;
  }

  isAtEnd(): boolean {
    return this.currentLine === this.section.endLine.index;
  }
}

export enum AssessmentType {
  veryGood,
  good,
  needsWork,
  poor
}
