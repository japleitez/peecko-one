import { FieldAccess } from '../../shared/profile/view.models';

export interface ILanguage {
  id: number;
  code?: string | null;
  name?: string | null;
  active?: boolean | null;
}

export type NewLanguage = Omit<ILanguage, 'id'> & { id: null };

export interface LanguageAccess {
  id: FieldAccess;
  code: FieldAccess;
  name: FieldAccess;
  active: FieldAccess;
}

export const LANGUAGE_ACCESS: LanguageAccess = {
  id: { listable: true, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  active: { listable: true, visible: true, disabled: false },
};
