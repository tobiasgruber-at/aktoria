import {Line, Role, SimpleScript} from './script-dtos';

export class SimpleSession {
  private lines: Line[];

  constructor(
    public id: number,
    public start: number,
    public end: number,
    //public selfAssessment: AssessmentType,
    //public deprecated: boolean,
    //public coverage: number,
    //public section: SimpleSection,
    public currentLine: number,
    public role: Role
  ) {
  }

  getLines(script: SimpleScript): Line[] {
    if (!this.lines && script) {
      this.lines = [];
      pagesLoop: for (const page of script?.pages) {
        for (const line of page.lines) {
          if (line.index > this.end) {
            break pagesLoop;
          }
          if (line.index >= this.start && line.active) {
            this.lines.push(line);
          }
        }
      }
    }
    return this.lines || [];
  }

  isAtStart(): boolean {
    return this.currentLine === this.start;
  }

  isAtEnd(): boolean {
    return this.currentLine === this.end;
  }
}

export enum AssessmentType {
  veryGood,
  good,
  needsWork,
  poor
}
