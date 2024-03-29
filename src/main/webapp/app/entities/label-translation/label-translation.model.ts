import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface ILabelTranslation {
  id: number;
  label?: string | null;
  lang?: keyof typeof Language | null;
  translation?: string | null;
}

export type NewLabelTranslation = Omit<ILabelTranslation, 'id'> & { id: null };

export interface LabelAccess {
  id: FieldAccess;
  label: FieldAccess;
  lang: FieldAccess;
  translation: FieldAccess;
}

export let LABEL_ACCESS: LabelAccess;

LABEL_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  label: { listable: true, visible: true, disabled: false },
  lang: { listable: true, visible: true, disabled: false },
  translation: { listable: true, visible: true, disabled: false },
};
