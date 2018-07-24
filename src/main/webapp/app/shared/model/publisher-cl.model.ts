import { Moment } from 'moment';

export interface IPublisherCl {
  id?: number;
  name?: string;
  website?: string;
  info?: string;
  imageContentType?: string;
  image?: any;
  creationDate?: Moment;
  lastUpdate?: Moment;
}

export const defaultValue: Readonly<IPublisherCl> = {};
