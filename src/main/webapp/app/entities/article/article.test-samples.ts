import dayjs from 'dayjs/esm';

import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 23254,
  code: 'cross aha',
  title: 'reconfirm uh-huh',
  language: 'FR',
};

export const sampleWithPartialData: IArticle = {
  id: 16151,
  code: 'briskly kind',
  title: 'rubbish randomisation than',
  subtitle: 'gosh gosh',
  language: 'FR',
  tags: 'perch fooey',
  duration: 442,
  chapter: 4834,
  archived: dayjs('2023-12-20T08:38'),
};

export const sampleWithFullData: IArticle = {
  id: 27243,
  code: 'smash without phew',
  title: 'spleen ew',
  subtitle: 'kaleidoscopic',
  summary: 'zowie infantilise',
  language: 'ES',
  tags: 'contaminate',
  duration: 18567,
  thumbnail: 'range beyond advice',
  audioUrl: 'satisfied midst',
  content: 'sedately valley vastly',
  seriesId: 32566,
  chapter: 19143,
  created: dayjs('2023-12-20T04:53'),
  updated: dayjs('2023-12-20T10:28'),
  released: dayjs('2023-12-20T12:03'),
  archived: dayjs('2023-12-20T13:04'),
};

export const sampleWithNewData: NewArticle = {
  code: 'mind every ultimately',
  title: 'gosh',
  language: 'ES',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
