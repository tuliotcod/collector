import { Moment } from 'moment';

export interface IPersonageCl {
  id?: number;
  name?: string;
  lastName?: string;
  codeName?: string;
  originalName?: string;
  bio?: string;
  creationDate?: Moment;
  lastUpdate?: Moment;
  countryId?: number;
  licensorId?: number;
  historyId?: number;
}

export const defaultValue: Readonly<IPersonageCl> = {};
