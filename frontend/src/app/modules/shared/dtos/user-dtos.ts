export class UserRegistration {
  constructor(
    public readonly firstName: string,
    public readonly lastName: string,
    public readonly email: string,
    public readonly password: string
  ) {
  }
}

export class SimpleUser {
  constructor(
    public readonly id: number,
    public readonly firstName: string,
    public readonly lastName: string,
    public readonly email: string,
    public readonly verified: boolean
  ) {
  }
}

export class DetailedUser extends SimpleUser {
  constructor(
    public readonly id: number,
    public readonly firstName: string,
    public readonly lastName: string,
    public readonly email: string,
    public readonly verified: boolean,
    public readonly passwordHash: string
  ) {
    super(id, firstName, lastName, email, verified);
    this.passwordHash = passwordHash;
  }
}

export class PasswordChange {
  constructor(
    public readonly oldPassword: string,
    public readonly newPassword: string
  ) {
  }
}
