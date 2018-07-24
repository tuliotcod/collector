import { Moment } from 'moment';

export interface IIssueInCollectionCl {
  id?: number;
  price?: number;
  amount?: number;
  notes?: string;
  creationDate?: Moment;
  lastUpdate?: Moment;
  issueId?: number;
  issueStatusId?: number;
  readingStatusId?: number;
  collectionId?: number;
}

export const defaultValue: Readonly<IIssueInCollectionCl> = {};
