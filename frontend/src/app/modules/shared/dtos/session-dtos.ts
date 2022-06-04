import { Line, Role, SimpleScript } from './script-dtos';
import { SimpleSection } from './section-dtos';

export class SimpleSession {
  private lines: Line[];
  private section: SimpleSection;
  private script: SimpleScript;

  constructor(
    public id: number,
    public start: string,
    public end: string,
    public selfAssessment: AssessmentType,
    public deprecated: boolean,
    public coverage: number,
    public sectionId: number,
    public currentLineIndex: number,
    public role: Role
  ) {}

  /** Inits the session. Should be done once after session fetched. */
  init(script: SimpleScript, section: SimpleSection): void {
    this.section = section;
    this.script = script;
  }

  getLines(): Line[] {
    if (!this.lines && this.script && this.section) {
      this.lines = [];
      pagesLoop: for (const page of this.script?.pages) {
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
    return this.currentLineIndex === this.section?.startLine.index;
  }

  isAtEnd(): boolean {
    return this.currentLineIndex === this.section?.endLine.index;
  }
}

export enum AssessmentType {
  veryGood,
  good,
  needsWork,
  poor
}

export class CreateSession {
  constructor(public sectionId: number, public roleId: number) {}
}

export class UpdateSession {
  constructor(
    public deprecated?: boolean,
    public selfAssessment?: AssessmentType,
    public currentLineId?: number
  ) {}
}
