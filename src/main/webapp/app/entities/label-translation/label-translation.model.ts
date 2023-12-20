import { Language } from 'app/entities/enumerations/language.model';

export interface ILabelTranslation {
  id: number;
  label?: string | null;
  lang?: keyof typeof Language | null;
  translation?: string | null;
}

export type NewLabelTranslation = Omit<ILabelTranslation, 'id'> & { id: null };
