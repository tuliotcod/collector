export interface IAddressCl {
  id?: number;
  city?: string;
  stateId?: number;
  countryId?: number;
}

export const defaultValue: Readonly<IAddressCl> = {};
