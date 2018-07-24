import { Moment } from 'moment';
import { IIssueInCollectionCl } from 'app/shared/model/collector/issue-in-collection-cl.model';

export interface ICollectionCl {
  id?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  issues?: IIssueInCollectionCl[];
}

export const defaultValue: Readonly<ICollectionCl> = {};
