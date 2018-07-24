import { Moment } from 'moment';

export interface ICurrencyCl {
  id?: number;
  symbol?: string;
  name?: string;
  startDate?: Moment;
  endDate?: Moment;
}

export const defaultValue: Readonly<ICurrencyCl> = {};
