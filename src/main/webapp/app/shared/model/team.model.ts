import { IProject } from 'app/shared/model/project.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITeam {
  id?: number;
  name?: string | null;
  project?: IProject;
  users?: IUser[];
}

export const defaultValue: Readonly<ITeam> = {};
