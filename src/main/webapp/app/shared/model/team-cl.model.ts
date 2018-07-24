import { Moment } from 'moment';

export interface ITeamCl {
  id?: number;
  name?: string;
  originalName?: string;
  bio?: string;
  creationDate?: Moment;
  lastUpdate?: Moment;
  countryId?: number;
  licensorId?: number;
}

export const defaultValue: Readonly<ITeamCl> = {};
