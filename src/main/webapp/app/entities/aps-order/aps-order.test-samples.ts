import { IApsOrder, NewApsOrder } from './aps-order.model';

export const sampleWithRequiredData: IApsOrder = {
  id: 18985,
  period: 16448,
  license: 'commerce wind',
  unitPrice: 3431.11,
  vatRate: 19214.73,
  numberOfUsers: 2750,
};

export const sampleWithPartialData: IApsOrder = {
  id: 8876,
  period: 31296,
  license: 'considering description whether',
  unitPrice: 32355.76,
  vatRate: 6644,
  numberOfUsers: 21760,
};

export const sampleWithFullData: IApsOrder = {
  id: 22426,
  period: 5774,
  license: 'bus brilliant',
  unitPrice: 8447.48,
  vatRate: 25846.74,
  numberOfUsers: 7754,
  invoiceNumber: 'what',
};

export const sampleWithNewData: NewApsOrder = {
  period: 25892,
  license: 'zowie',
  unitPrice: 3301.32,
  vatRate: 27461.86,
  numberOfUsers: 31368,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
