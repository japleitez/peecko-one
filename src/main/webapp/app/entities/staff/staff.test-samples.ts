import { IStaff, NewStaff } from './staff.model';

export const sampleWithRequiredData: IStaff = {
  id: 28955,
  userId: 11880,
  role: 'curb humiliating',
};

export const sampleWithPartialData: IStaff = {
  id: 31838,
  userId: 4935,
  role: 'by zowie',
};

export const sampleWithFullData: IStaff = {
  id: 32384,
  userId: 29738,
  role: 'portfolio',
};

export const sampleWithNewData: NewStaff = {
  userId: 29174,
  role: 'though humming duplicate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
