import { Moment } from 'moment';
import { ICollaboratorCl } from 'app/shared/model/collector/collaborator-cl.model';
import { IPersonageCl } from 'app/shared/model/collector/personage-cl.model';

export interface IHistoryCl {
  id?: number;
  order?: number;
  name?: string;
  pages?: number;
  desc?: string;
  part?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  issueId?: number;
  arcId?: number;
  originalIssueId?: number;
  collaborators?: ICollaboratorCl[];
  characters?: IPersonageCl[];
}

export const defaultValue: Readonly<IHistoryCl> = {};
