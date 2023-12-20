import dayjs from 'dayjs/esm';

import { IApsUser, NewApsUser } from './aps-user.model';

export const sampleWithRequiredData: IApsUser = {
  id: 26867,
  name: 'psst snoopy when',
  username: 'creepy rhythm',
  usernameVerified: false,
  privateEmail: 'upward',
  privateVerified: false,
  language: 'FR',
  active: false,
};

export const sampleWithPartialData: IApsUser = {
  id: 14736,
  name: 'jolly pointless step-son',
  username: 'along alongside excitement',
  usernameVerified: true,
  privateEmail: 'limber phooey pish',
  privateVerified: true,
  language: 'EN',
  active: false,
  password: 'wherever yahoo exorcise',
};

export const sampleWithFullData: IApsUser = {
  id: 498,
  name: 'lest clearly metallurgist',
  username: 'well-informed',
  usernameVerified: true,
  privateEmail: 'overestimate service',
  privateVerified: false,
  language: 'DE',
  license: 'incidentally besides',
  active: true,
  password: 'how incidentally packetize',
  created: dayjs('2023-12-20T03:41'),
  updated: dayjs('2023-12-20T01:48'),
};

export const sampleWithNewData: NewApsUser = {
  name: 'versus onto',
  username: 'lanky',
  usernameVerified: false,
  privateEmail: 'offensively hm',
  privateVerified: false,
  language: 'FR',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
