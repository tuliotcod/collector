import { Moment } from 'moment';

export interface ITitleCl {
  id?: number;
  name?: string;
  serie?: string;
  startDate?: Moment;
  endDate?: Moment;
  info?: string;
  creationDate?: Moment;
  lastUpdate?: Moment;
  categoryId?: number;
  genreId?: number;
  statusId?: number;
}

export const defaultValue: Readonly<ITitleCl> = {};
