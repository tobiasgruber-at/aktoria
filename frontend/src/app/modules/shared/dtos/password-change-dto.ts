export class ChangePassword {
  constructor(
    public readonly token: string,
    public readonly oldPassword: string,
    public readonly newPassword: string
  ) {}
}
