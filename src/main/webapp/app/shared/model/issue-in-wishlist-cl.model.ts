import { Moment } from 'moment';

export interface IIssueInWishlistCl {
  id?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  wishlistId?: number;
}

export const defaultValue: Readonly<IIssueInWishlistCl> = {};
