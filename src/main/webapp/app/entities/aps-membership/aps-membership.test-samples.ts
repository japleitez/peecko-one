import { IApsMembership, NewApsMembership } from './aps-membership.model';

export const sampleWithRequiredData: IApsMembership = {
  id: 789,
  period: 28140,
  license: 'infuriate',
  username: 'art yearly maul',
};

export const sampleWithPartialData: IApsMembership = {
  id: 26878,
  period: 21105,
  license: 'min yearly pawnshop',
  username: 'hearken coolly',
};

export const sampleWithFullData: IApsMembership = {
  id: 19735,
  period: 3946,
  license: 'sizzling versus egghead',
  username: 'eek inquisitively',
};

export const sampleWithNewData: NewApsMembership = {
  period: 8831,
  license: 'whoever',
  username: 'whether pinpoint',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
