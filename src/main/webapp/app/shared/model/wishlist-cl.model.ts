import { Moment } from 'moment';
import { IIssueInWishlistCl } from 'app/shared/model/collector/issue-in-wishlist-cl.model';

export interface IWishlistCl {
  id?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  issues?: IIssueInWishlistCl[];
}

export const defaultValue: Readonly<IWishlistCl> = {};
