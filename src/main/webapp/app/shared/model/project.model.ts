export interface IProject {
  id?: number;
  name?: string | null;
  code?: string;
}

export const defaultValue: Readonly<IProject> = {};
