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

class Page {
  index: number;
  lines: Line[];
}

class Line {
  index: number;
  role: Role;
  content: string;
  audioSnippet: AudioBuffer;
  active: boolean;
}

class Role {
  id: number;
  name: string;
  color: string;
}
