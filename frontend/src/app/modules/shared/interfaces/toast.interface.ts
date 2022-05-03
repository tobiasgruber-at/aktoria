export interface Toast {
  title?: string;
  type: 'success' | 'warning' | 'danger' | 'primary';
  message: string;
}
