import {SimpleUser} from './user-dtos';

export class UploadScript {
  constructor(public readonly file: File) {}
}

export class SimpleScript {
  constructor(
    public pages: Page[],
    public roles: Role[],
    public name: string
  ) {}

  getId(): number {
    return null;
  }
}

export class ScriptPreview {
  constructor(public readonly id: number, public readonly name: string) {}
}

export class DetailedScript extends SimpleScript {
  owner: SimpleUser;
  participants: SimpleUser[];

  constructor(
    public readonly id: number,
    pages: Page[],
    roles: Role[],
    name: string,
    owner: SimpleUser,
    participants: SimpleUser[]
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
  id?: number;
  index: number;
  roles: Role[];
  content: string;
  audioSnippet: AudioBuffer;
  active: boolean;
}

export class UpdateLine {
  content?: string;
  active?: boolean;
  roleIds?: number[];
}

export class Role {
  id: number;
  name: string;
  color: string;
}

export class MergeRoles {
  ids: number[];
  newName: string;
}
