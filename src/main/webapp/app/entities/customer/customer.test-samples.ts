import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 5425,
  code: 'zowie',
  name: 'frantically vice',
  country: 'Faroe Islands',
  state: 'ACTIVE',
};

export const sampleWithPartialData: ICustomer = {
  id: 582,
  code: 'um',
  name: 'worn toward gobble',
  country: 'Virgin Islands, British',
  state: 'TRIAL',
  license: 'why wherever unsteady',
  bank: 'truthfully ha',
  logo: 'however',
  notes: 'white',
  closed: dayjs('2023-12-20T01:54'),
};

export const sampleWithFullData: ICustomer = {
  id: 31956,
  code: 'defensive aw',
  name: 'usable mockingly old-fashioned',
  country: 'Qatar',
  state: 'ACTIVE',
  license: 'or dance',
  billingEmail: 'poison so round',
  vatId: 'researcher',
  bank: 'how intermix ah',
  iban: 'PK52KUWZ6006857025304091',
  logo: 'bah after rescue',
  notes: 'since',
  created: dayjs('2023-12-19T22:17'),
  updated: dayjs('2023-12-20T12:16'),
  trialed: dayjs('2023-12-20T08:00'),
  declined: dayjs('2023-12-20T04:03'),
  activated: dayjs('2023-12-20T09:05'),
  closed: dayjs('2023-12-20T14:00'),
};

export const sampleWithNewData: NewCustomer = {
  code: 'yum apropos peak',
  name: 'an bristle broiler',
  country: 'New Caledonia',
  state: 'TRIAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
