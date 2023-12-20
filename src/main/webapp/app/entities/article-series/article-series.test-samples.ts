import dayjs from 'dayjs/esm';

import { IArticleSeries, NewArticleSeries } from './article-series.model';

export const sampleWithRequiredData: IArticleSeries = {
  id: 7600,
  code: 'in book',
  title: 'shimmering slang',
  language: 'DE',
  counter: 28779,
};

export const sampleWithPartialData: IArticleSeries = {
  id: 23277,
  code: 'about',
  title: 'ouch',
  summary: 'meh',
  language: 'ES',
  tags: 'yearly accidentally ouch',
  counter: 10217,
  updated: dayjs('2023-12-20T13:40'),
  archived: dayjs('2023-12-20T05:18'),
};

export const sampleWithFullData: IArticleSeries = {
  id: 30545,
  code: 'intermix adored',
  title: 'colorfully codify jealously',
  subtitle: 'obediently as firebomb',
  summary: 'sociology',
  language: 'FR',
  tags: 'leap shadowy',
  thumbnail: 'supposing soupy along',
  counter: 18942,
  created: dayjs('2023-12-19T22:58'),
  updated: dayjs('2023-12-20T07:29'),
  released: dayjs('2023-12-20T02:55'),
  archived: dayjs('2023-12-20T10:53'),
};

export const sampleWithNewData: NewArticleSeries = {
  code: 'phooey but',
  title: 'valid sow fibre',
  language: 'DE',
  counter: 23295,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
