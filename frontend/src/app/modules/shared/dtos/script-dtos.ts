export class UploadScript {
  constructor(public readonly file: File) {}
}

export class DeleteScriptRequest {
  constructor(public readonly id: number) {}
}

export class SimpleScript {
  protected pages: Page[];
  protected roles: Role[];
  protected name: string;
}

export class DetailedScript extends SimpleScript {
  private id: number;
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
