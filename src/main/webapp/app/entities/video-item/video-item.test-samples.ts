import { IVideoItem, NewVideoItem } from './video-item.model';

export const sampleWithRequiredData: IVideoItem = {
  id: 7258,
};

export const sampleWithPartialData: IVideoItem = {
  id: 6378,
  previous: 'furthermore',
};

export const sampleWithFullData: IVideoItem = {
  id: 743,
  previous: 'empire great-grandmother tensely',
  code: 'minus',
  next: 'cruelly mainstream poll',
};

export const sampleWithNewData: NewVideoItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
