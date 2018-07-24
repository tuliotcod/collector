import { Moment } from 'moment';

export interface IArtistCl {
  id?: number;
  name?: string;
  lastName?: string;
  nickname?: string;
  birthday?: Moment;
  dateOfDeath?: Moment;
  bio?: string;
  imageContentType?: string;
  image?: any;
  creationDate?: Moment;
  lastUpdate?: Moment;
  countryId?: number;
}

export const defaultValue: Readonly<IArtistCl> = {};
