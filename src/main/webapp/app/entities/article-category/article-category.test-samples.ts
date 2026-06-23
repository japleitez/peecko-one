import dayjs from 'dayjs/esm';

import { IArticleCategory, NewArticleCategory } from './article-category.model';

export const sampleWithRequiredData: IArticleCategory = {
  id: 14103,
  code: 'and affirm chunter',
  title: 'lanky disgust if',
};

export const sampleWithPartialData: IArticleCategory = {
  id: 23035,
  code: 'vanquish chard',
  title: 'webinar wetly',
  release: dayjs('2023-12-20T05:26'),
  archived: dayjs('2023-12-20T10:07'),
};

export const sampleWithFullData: IArticleCategory = {
  id: 31937,
  code: 'anniversary',
  title: 'crossly wafer',
  created: dayjs('2023-12-20T00:13'),
  release: dayjs('2023-12-20T03:42'),
  archived: dayjs('2023-12-20T14:54'),
};

export const sampleWithNewData: NewArticleCategory = {
  code: 'norm but oof',
  title: 'woot onto wisely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
