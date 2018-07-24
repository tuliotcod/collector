import { Moment } from 'moment';

export interface ICommentCl {
  id?: number;
  date?: Moment;
  comment?: string;
  issueId?: number;
}

export const defaultValue: Readonly<ICommentCl> = {};
