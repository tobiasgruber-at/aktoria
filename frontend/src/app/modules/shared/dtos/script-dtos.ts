export class UploadScript {
  constructor(public readonly file: File) {}
}

export class DeleteScriptRequest {
  constructor(public readonly id: number) {}
}

export class SimpleScript {
  constructor(
    public readonly pages: Page[],
    public readonly roles: Role[],
    public readonly name: string
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
  index: number;
  roles: Role[];
  content: string;
  audioSnippet: AudioBuffer;
  active: boolean;
}

export class Role {
  id: number;
  name: string;
  color: string;
}
