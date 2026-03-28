import dayjs from 'dayjs/esm';

import { IVideoCategory, NewVideoCategory } from './video-category.model';

export const sampleWithRequiredData: IVideoCategory = {
  id: 13994,
  pos: 0,
  code: 'evoke loosely suddenly',
  title: 'year',
};

export const sampleWithPartialData: IVideoCategory = {
  id: 31902,
  pos: 0,
  code: 'rejuvenate worth',
  title: 'while heir hilarious',
  created: dayjs('2023-12-20T03:46'),
  archived: dayjs('2023-12-19T23:50'),
};

export const sampleWithFullData: IVideoCategory = {
  id: 17507,
  pos: 0,
  code: 'boohoo',
  title: 'nor brr',
  created: dayjs('2023-12-19T20:17'),
  released: dayjs('2023-12-20T06:40'),
  archived: dayjs('2023-12-20T03:03'),
};

export const sampleWithNewData: NewVideoCategory = {
  pos: 0,
  code: 'needily',
  title: 'greedy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
