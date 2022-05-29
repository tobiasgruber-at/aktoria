import {SimpleScript} from './script-dtos';

export class SimpleSection {
  private startPage: number = null;
  private endPage: number = null;

  constructor(public name, public startLine, public endLine) {
  }

  getStartPage(script: SimpleScript): number {
    if (!this.startPage) {
      this.startPage = script?.pages.find((p) =>
        p.lines.some((l) => l.index === this.startLine)
      )?.index;
    }
    return this.startPage;
  }

  getEndPage(script: SimpleScript): number {
    if (!this.endPage) {
      this.endPage = script?.pages.find((p) =>
        p.lines.some((l) => l.index === this.endLine)
      )?.index;
    }
    return this.endPage;
  }
}
