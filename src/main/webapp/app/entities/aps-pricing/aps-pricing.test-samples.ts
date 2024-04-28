import { IApsPricing, NewApsPricing } from './aps-pricing.model';

export const sampleWithRequiredData: IApsPricing = {
  id: 4944,
  index: 31473,
  minQuantity: 23192,
  unitPrice: 1144.79,
};

export const sampleWithPartialData: IApsPricing = {
  id: 10316,
  index: 11627,
  minQuantity: 30439,
  unitPrice: 30295.92,
};

export const sampleWithFullData: IApsPricing = {
  id: 10756,
  index: 12610,
  minQuantity: 29688,
  unitPrice: 16810.83,
};

export const sampleWithNewData: NewApsPricing = {
  index: 11878,
  minQuantity: 11084,
  unitPrice: 32687.06,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
