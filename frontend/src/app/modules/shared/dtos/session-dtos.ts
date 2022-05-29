import { Line, Role, SimpleScript } from './script-dtos';
import { SimpleSection } from './section-dtos';

export class SimpleSession {
  private lines: Line[];
  private section: SimpleSection;
  private script: SimpleScript;
  private role: Role;

  constructor(
    public id: number,
    public start: Date,
    public end: Date,
    public selfAssessment: AssessmentType,
    public deprecated: boolean,
    public coverage: number,
    public sectionId: number,
    public currentLineIndex: number,
    public roleId: number
  ) {}

  setScript(script: SimpleScript): void {
    this.script = script;
  }

  /** Sets the section. Should be done once after session fetched. */
  setSection(section: SimpleSection): void {
    this.section = section;
  }

  getRole(): Role {
    return (
      this.role || this.script?.roles.find((r) => r.id === this.roleId) || null
    );
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
