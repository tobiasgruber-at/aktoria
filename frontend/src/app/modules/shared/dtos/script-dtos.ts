import { SimpleUser } from './user-dtos';

export class UploadScript {
  constructor(public readonly file: File) {}
}

export class SimpleScript {
  private lastLineIdx: number = null;

  constructor(
    public pages: Page[],
    public roles: Role[],
    public name: string
  ) {}

  getId(): number {
    return null;
  }

  getLastLineIdx(): number {
    if (!this.lastLineIdx) {
      const lastPage = this.pages[this.pages.length - 1];
      const lastLine = lastPage.lines[lastPage.lines.length - 1];
      this.lastLineIdx = lastLine.index;
    }
    return this.lastLineIdx;
  }
}

export class ScriptPreview {
  constructor(
    public readonly id: number,
    public readonly name: string,
    public readonly owner: boolean
  ) {}
}

export class DetailedScript extends SimpleScript {
  constructor(
    public readonly id: number,
    public owner: SimpleUser,
    public participants: SimpleUser[],
    pages: Page[],
    roles: Role[],
    name: string
  ) {
    super(pages, roles, name);
  }

  getId(): number {
    return this.id;
  }
}

export class Page {
  index: number;
  lines: Line[];
}

export class Line {
  constructor(
    public index: number,
    public roles: Role[],
    public content: string,
    public audio: AudioBuffer,
    public recordedBy: Role,
    public active: boolean,
    public conflictType: Conflict,
    public id?: number
  ) {}
}

enum Conflict {
  verificationRequired,
  assignmentRequired
}

export class UpdateLine {
  content?: string;
  active?: boolean;
  roleIds?: number[];
}

export class Role {
  constructor(public id: number, public name: string, public color: string) {}
}

export class MergeRoles {
  ids: number[];
  newName: string;
}
