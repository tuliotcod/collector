import { Moment } from 'moment';

export interface ICollaboratorCl {
  id?: number;
  creationDate?: Moment;
  lastUpdate?: Moment;
  issueId?: number;
  historyId?: number;
  artistId?: number;
  functionId?: number;
}

export const defaultValue: Readonly<ICollaboratorCl> = {};
