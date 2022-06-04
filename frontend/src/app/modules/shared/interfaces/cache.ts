export type Cache<T extends { id: number }> = {
  [key in T['id']]: T;
};
