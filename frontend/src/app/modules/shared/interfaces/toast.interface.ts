import { Theme } from '../enums/theme.enum';

export interface Toast {
  title?: string;
  theme: Theme;
  message: string;
}
