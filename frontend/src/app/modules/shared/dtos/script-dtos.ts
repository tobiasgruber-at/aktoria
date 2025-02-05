import {SimpleUser} from './user-dtos';

export class UploadScript {
  constructor(public readonly file: File) {
  }
}

export class SimpleScript {
  private lastLineIdx: number = null;

  constructor(
    public pages: Page[],
    public roles: Role[],
    public name: string
  ) {
  }

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

export class RawSimpleScript {
  private lastLineIdx: number = null;

  constructor(
    public pages: RawPage[],
    public roles: Role[],
    public name: string
  ) {
  }

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
  ) {
  }
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

export class RawDetailedScript extends RawSimpleScript {
  constructor(
    public readonly id: number,
    public owner: SimpleUser,
    public participants: SimpleUser[],
    pages: RawPage[],
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
  constructor(public index: number, public lines: Line[]) {
  }
}

export class RawPage {
  constructor(public index: number, public lines: RawLine[]) {
  }
}

export class Line {
  public temporaryRecording: Blob = null;
  public temporaryRecordingUrl: string = null;

  constructor(
    public index: number,
    public roles: Role[],
    public content: string,
    public audio: Blob,
    public recordedBy: Role,
    public active: boolean,
    public conflictType?: Conflict,
    public id?: number
  ) {
  }
}

export class RawLine {
  public temporaryRecording: Blob = null;
  public temporaryRecordingUrl: string = null;

  constructor(
    public index: number,
    public roles: Role[],
    public content: string,
    public audio: string,
    public recordedBy: Role,
    public active: boolean,
    public conflictType?: Conflict,
    public id?: number
  ) {
  }
}

export enum Conflict {
  verificationRequired = 'VERIFICATION_REQUIRED',
  assignmentRequired = 'ASSIGNMENT_REQUIRED'
}

export class UpdateLine {
  content?: string;
  active?: boolean;
  roleIds?: number[];
  audio?: string | ArrayBuffer;
}

export class Role {
  constructor(public id: number, public name: string, public color: string) {
  }
}

export class MergeRoles {
  ids: number[];
  newName: string;
}
