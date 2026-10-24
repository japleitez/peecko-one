import { FieldAccess } from '../../shared/profile/view.models';

export interface ICountry {
  code: string;
  name: string | null;
  locale?: string | null;
  currency?: string | null;
  language?: string | null;
}

export type NewCountry = Omit<ICountry, 'code'> & { code: null };

export interface CountryAccess {
  code: FieldAccess;
  name: FieldAccess;
  locale: FieldAccess;
  currency: FieldAccess;
  language: FieldAccess;
}

export const COUNTRY_ACCESS: CountryAccess = {
  code: { listable: true, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  locale: { listable: true, visible: true, disabled: false },
  currency: { listable: true, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
};
