import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IVacationRequest {
  id?: number;
  description?: string | null;
  date?: string;
  user?: IUser;
  approbedBies?: IUser[];
}

export const defaultValue: Readonly<IVacationRequest> = {};
