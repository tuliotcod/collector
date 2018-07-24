import { Moment } from 'moment';
import { ICollaboratorCl } from 'app/shared/model/collector/collaborator-cl.model';
import { IHistoryCl } from 'app/shared/model/collector/history-cl.model';
import { ICommentCl } from 'app/shared/model/collector/comment-cl.model';

export interface IIssueCl {
  id?: number;
  number?: number;
  info?: string;
  coverContentType?: string;
  cover?: any;
  month?: number;
  year?: number;
  day?: number;
  pages?: number;
  sameFormatAllIssues?: boolean;
  coverPrice?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  titleId?: number;
  coverCollectorUserId?: number;
  formatId?: number;
  finishingId?: number;
  currencyId?: number;
  countryId?: number;
  collaborators?: ICollaboratorCl[];
  histories?: IHistoryCl[];
  comments?: ICommentCl[];
}

export const defaultValue: Readonly<IIssueCl> = {
  sameFormatAllIssues: false
};
