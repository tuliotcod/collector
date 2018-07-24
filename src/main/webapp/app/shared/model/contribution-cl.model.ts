import { Moment } from 'moment';

export interface IContributionCl {
  id?: number;
  creationDate?: Moment;
  typeId?: number;
}

export const defaultValue: Readonly<IContributionCl> = {};
