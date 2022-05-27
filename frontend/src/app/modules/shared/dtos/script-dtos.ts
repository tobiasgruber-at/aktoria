export class UploadScript {
  constructor(public readonly file: File) {}
}

export class SimpleScript {
  constructor(
    public pages: Page[],
    public roles: Role[],
    public name: string
  ) {}
}

export class ScriptPreview {
  constructor(public readonly id: number, public readonly name: string) {}
}

export class DetailedScript extends SimpleScript {
  constructor(
    public readonly id: number,
    pages: Page[],
    roles: Role[],
    name: string
  ) {
    super(pages, roles, name);
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
