import { Moment } from 'moment';

export interface ICollectorUserCl {
  id?: number;
  username?: string;
  email?: string;
  password?: string;
  name?: string;
  lastName?: string;
  birthday?: Moment;
  gender?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
}

export const defaultValue: Readonly<ICollectorUserCl> = {};
