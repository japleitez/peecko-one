import { ILabelTranslation, NewLabelTranslation } from './label-translation.model';

export const sampleWithRequiredData: ILabelTranslation = {
  id: 14298,
  label: 'up yum',
  lang: 'EN',
  translation: 'whoever roof',
};

export const sampleWithPartialData: ILabelTranslation = {
  id: 6503,
  label: 'aw rapidly',
  lang: 'FR',
  translation: 'sewer postbox which',
};

export const sampleWithFullData: ILabelTranslation = {
  id: 30426,
  label: 'diner yuck',
  lang: 'FR',
  translation: 'livid nervous wetly',
};

export const sampleWithNewData: NewLabelTranslation = {
  label: 'or hopeful',
  lang: 'EN',
  translation: 'where overcome punctually',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
