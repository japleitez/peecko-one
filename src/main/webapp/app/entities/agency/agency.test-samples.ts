import dayjs from 'dayjs/esm';

import { IAgency, NewAgency } from './agency.model';

export const sampleWithRequiredData: IAgency = {
  id: 22184,
  code: 'who compassion',
  name: 'an',
  city: 'Keonhaven',
  country: 'Algeria',
  language: 'DE',
};

export const sampleWithPartialData: IAgency = {
  id: 23309,
  code: 'quarry province grimy',
  name: 'virtual',
  line1: 'gracefully',
  city: 'Clydechester',
  country: 'Madagascar',
  language: 'DE',
  email: 'Felicita_Reichel48@yahoo.com',
  phone: '1-811-715-4484 x8841',
  billingEmail: 'blight grease fibrosis',
  billingPhone: 'safeguard',
  bank: 'after sarcastic',
  vatId: 'failing',
  created: dayjs('2023-12-20T08:46'),
  updated: dayjs('2023-12-20T08:19'),
};

export const sampleWithFullData: IAgency = {
  id: 2283,
  code: 'instead app circulate',
  name: 'than',
  line1: 'near what',
  line2: 'tremendously bidet apropos',
  zip: 'aw pin',
  city: 'Cutler Bay',
  country: 'Guyana',
  language: 'FR',
  email: 'Friedrich13@yahoo.com',
  phone: '420-804-4401 x2525',
  billingEmail: 'philosophise',
  billingPhone: 'weakly',
  bank: 'bay wide shady',
  iban: 'MT93UXXK010207869X72B975T672887',
  rcs: 'pushy macaw',
  vatId: 'forenenst after',
  vatRate: 27634.55,
  notes: 'marketplace',
  created: dayjs('2023-12-20T07:06'),
  updated: dayjs('2023-12-20T15:28'),
};

export const sampleWithNewData: NewAgency = {
  code: 'uh-huh training',
  name: 'incidentally astride than',
  city: 'Euless',
  country: 'Aruba',
  language: 'ES',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
