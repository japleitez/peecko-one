import dayjs from 'dayjs/esm';

import { IVideo, NewVideo } from './video.model';

export const sampleWithRequiredData: IVideo = {
  id: 3524,
  code: 'mmm teethe dude',
  title: 'quarrelsomely',
  language: 'ES',
  player: 'PEECKO',
};

export const sampleWithPartialData: IVideo = {
  id: 765,
  code: 'rigidly',
  title: 'meanwhile unique warmly',
  language: 'DE',
  player: 'PEECKO',
  thumbnail: 'cobweb psst',
  url: 'https://large-purchase.name/',
  audience: 'who consignment',
  description: 'caress',
  created: dayjs('2023-12-20T04:15'),
  archived: dayjs('2023-12-20T05:54'),
};

export const sampleWithFullData: IVideo = {
  id: 29902,
  code: 'across consequently gah',
  title: 'inside tensely',
  duration: 11098,
  language: 'EN',
  tags: 'for vacantly',
  player: 'PEECKO',
  thumbnail: 'although within potty',
  url: 'https://zealous-blight.org/',
  audience: 'bah supposing',
  intensity: 'INTERMEDIATE',
  filename: 'beautifully',
  description: 'blight',
  created: dayjs('2023-12-20T04:27'),
  released: dayjs('2023-12-20T09:12'),
  archived: dayjs('2023-12-19T23:31'),
};

export const sampleWithNewData: NewVideo = {
  code: 'sundial',
  title: 'over',
  language: 'ES',
  player: 'PEECKO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
