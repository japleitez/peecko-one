import { ILabelTranslation, NewLabelTranslation } from './label-translation.model';

export const sampleWithRequiredData: ILabelTranslation = {
  id: 14298,
  code: 'up yum',
  lang: 'EN',
  text: 'whoever roof',
};

export const sampleWithPartialData: ILabelTranslation = {
  id: 6503,
  code: 'aw rapidly',
  lang: 'FR',
  text: 'sewer postbox which',
};

export const sampleWithFullData: ILabelTranslation = {
  id: 30426,
  code: 'diner yuck',
  lang: 'FR',
  text: 'livid nervous wetly',
};

export const sampleWithNewData: NewLabelTranslation = {
  code: 'or hopeful',
  lang: 'EN',
  text: 'where overcome punctually',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
