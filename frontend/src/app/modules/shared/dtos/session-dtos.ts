import { Line, Role, SimpleScript } from './script-dtos';
import { SimpleSection } from './section-dtos';
import { BehaviorSubject, Observable } from 'rxjs';

export class SimpleSession {
  private lines: Line[];
  private section: SimpleSection;
  private script: SimpleScript;
  private currentLineIndexSubject: BehaviorSubject<number>;

  constructor(
    public id: number,
    public start: string,
    public end: string,
    public selfAssessment: AssessmentType,
    public deprecated: boolean,
    public coverage: number,
    public sectionId: number,
    currentLineIndex: number,
    public role: Role
  ) {
    this.currentLineIndexSubject = new BehaviorSubject<number>(
      currentLineIndex
    );
  }

  get $currentLineIndex(): Observable<number> {
    return this.currentLineIndexSubject.asObservable();
  }

  get currentLineIndex(): number {
    return this.currentLineIndexSubject.getValue();
  }

  set currentLineIndex(idx: number) {
    this.currentLineIndexSubject.next(idx);
  }

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

  getCurrentLine(): Line {
    if (this.lines && this.script && this.section) {
      pagesLoop: for (const page of this.script?.pages) {
        for (const line of page.lines) {
          if (line.index > this.section.endLine.index) {
            break pagesLoop;
          }
          if (line.index === this.currentLineIndex) {
            return line;
          }
        }
      }
    }
    return null;
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
