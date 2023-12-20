import dayjs from 'dayjs/esm';

import { IPlayList, NewPlayList } from './play-list.model';

export const sampleWithRequiredData: IPlayList = {
  id: 10445,
  name: 'unique skive',
  counter: 27764,
  created: dayjs('2023-12-19T18:49'),
  updated: dayjs('2023-12-20T04:20'),
};

export const sampleWithPartialData: IPlayList = {
  id: 18664,
  name: 'seclude weekender definitive',
  counter: 8,
  created: dayjs('2023-12-20T08:01'),
  updated: dayjs('2023-12-20T01:35'),
};

export const sampleWithFullData: IPlayList = {
  id: 423,
  name: 'monthly',
  counter: 28274,
  created: dayjs('2023-12-20T17:27'),
  updated: dayjs('2023-12-20T08:11'),
};

export const sampleWithNewData: NewPlayList = {
  name: 'enfeeble toothpaste quaver',
  counter: 11152,
  created: dayjs('2023-12-20T16:46'),
  updated: dayjs('2023-12-20T08:01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
