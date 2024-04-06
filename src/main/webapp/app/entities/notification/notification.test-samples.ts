import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 5860,
  title: 'at',
  message: 'duh mushy',
  language: 'DE',
};

export const sampleWithPartialData: INotification = {
  id: 28856,
  title: 'repeatedly',
  message: 'incidentally',
  language: 'DE',
  imageUrl: 'failing charter',
  videoUrl: 'upward dimly',
  starts: dayjs('2023-12-19'),
};

export const sampleWithFullData: INotification = {
  id: 7618,
  title: 'injure boohoo',
  message: 'feature shakily foam',
  language: 'ES',
  imageUrl: 'lined incidentally',
  videoUrl: 'shyly drab',
  starts: dayjs('2023-12-20'),
  expires: dayjs('2023-12-20'),
};

export const sampleWithNewData: NewNotification = {
  title: 'between noisily',
  message: 'spur as elastic',
  language: 'ES',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
