import { Line, SimpleScript } from './script-dtos';

export class SimpleSection {
  private startPage: number = null;
  private endPage: number = null;

  constructor(
    public name: string,
    public startLine: Line,
    public endLine: Line,
    public id?: number
  ) {}

  getStartPage(script: SimpleScript): number {
    if (!this.startPage) {
      this.startPage = script?.pages.find((p) =>
        p.lines.some((l) => l.index === this.startLine.index)
      )?.index;
    }
    return this.startPage;
  }

  getEndPage(script: SimpleScript): number {
    if (!this.endPage) {
      this.endPage = script?.pages.find((p) =>
        p.lines.some((l) => l.index === this.endLine.index)
      )?.index;
    }
    return this.endPage;
  }
}

export class CreateSection {
  constructor(
    public name: string,
    public ownerId: number,
    public startLineId: number,
    public endLineId: number,
    public sessionIds: number[]
  ) {}
}
