export class AudioNotAllowedError extends Error {
  constructor(msg: string) {
    super(msg);
    // Set the prototype explicitly.
    Object.setPrototypeOf(this, AudioNotAllowedError.prototype);
  }
}

export class AudioNotSupportedError extends Error {
  constructor(msg: string) {
    super(msg);
    // Set the prototype explicitly.
    Object.setPrototypeOf(this, AudioNotSupportedError.prototype);
  }
}
