import dayjs from 'dayjs/esm';

import { IContact, NewContact } from './contact.model';

export const sampleWithRequiredData: IContact = {
  id: 26499,
  type: 'OTHER',
  name: 'afore',
};

export const sampleWithPartialData: IContact = {
  id: 2965,
  type: 'PRIMARY',
  name: 'late',
  line1: 'midst oh',
  zip: 'ha needily',
  email: 'Lexi12@gmail.com',
  created: dayjs('2023-12-19T19:26'),
};

export const sampleWithFullData: IContact = {
  id: 22477,
  type: 'PRIMARY',
  name: 'times apprehensive valiantly',
  line1: 'below',
  line2: 'quizzically enemy',
  zip: 'compact serious punishment',
  city: 'Shanonchester',
  country: 'Anguilla',
  email: 'Annabelle.Streich@gmail.com',
  phone: '436.625.5346 x66670',
  notes: 'emend',
  created: dayjs('2023-12-19T20:35'),
  updated: dayjs('2023-12-19T19:26'),
};

export const sampleWithNewData: NewContact = {
  type: 'PRIMARY',
  name: 'downright sore',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
