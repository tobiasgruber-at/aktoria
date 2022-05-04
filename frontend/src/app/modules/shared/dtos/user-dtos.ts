export class UserRegistration {
  constructor(
    public readonly name: string,
    public readonly email: string,
    public readonly password: string
  ) {}
}

export class PasswordChange {
  constructor(
    public readonly oldPassword: string,
    public readonly newPassword: string
  ) {}
}

export class SimpleUser {
  constructor(
    public readonly id: number,
    public readonly name: string,
    public readonly email: string,
    public readonly verified: boolean
  ) {}
}

export class AuthenticatedUser extends SimpleUser {
  constructor(
    public readonly id: number,
    public readonly name: string,
    public readonly email: string,
    public readonly verified: boolean,
    public readonly jwtToken: string
  ) {
    super(id, name, email, verified);
  }
}
