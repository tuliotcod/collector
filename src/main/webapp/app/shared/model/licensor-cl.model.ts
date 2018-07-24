import { Moment } from 'moment';

export interface ILicensorCl {
  id?: number;
  name?: string;
  website?: string;
  info?: string;
  imageContentType?: string;
  image?: any;
  creationDate?: Moment;
  lastUpdate?: Moment;
}

export const defaultValue: Readonly<ILicensorCl> = {};
