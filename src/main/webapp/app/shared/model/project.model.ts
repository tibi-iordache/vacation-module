import { IUser } from 'app/shared/model/user.model';

export interface IProject {
  id?: number;
  name?: string | null;
  code?: string;
  projectManager?: IUser | null;
  techLead?: IUser | null;
}

export const defaultValue: Readonly<IProject> = {};
