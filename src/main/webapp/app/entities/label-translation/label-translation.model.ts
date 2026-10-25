import { FieldAccess } from '../../shared/profile/view.models';

export interface ILabelTranslation {
  id: number;
  code?: string | null;
  lang?: string | null;
  text?: string | null;
}

export type NewLabelTranslation = Omit<ILabelTranslation, 'id'> & { id: null };

export interface LabelAccess {
  id: FieldAccess;
  code: FieldAccess;
  lang: FieldAccess;
  text: FieldAccess;
}

export const LABEL_ACCESS: LabelAccess = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  lang: { listable: true, visible: true, disabled: false },
  text: { listable: true, visible: true, disabled: false },
};
