import { Moment } from 'moment';

export interface IArcCl {
  id?: number;
  name?: string;
  info?: string;
  creationDate?: Moment;
  lastUpdate?: Moment;
}

export const defaultValue: Readonly<IArcCl> = {};
