import { IInvoiceItem, NewInvoiceItem } from './invoice-item.model';

export const sampleWithRequiredData: IInvoiceItem = {
  id: 18196,
  type: 'AMS',
  description: 'correctly like bat',
  quantity: 10920,
  unitPrice: 17209.68,
  priceExtended: 28497.18,
  disRate: 1596.74,
  disAmount: 20801.75,
  subtotal: 28541.42,
  vatRate: 20201.02,
  vat: 27081.42,
  total: 26978.71,
};

export const sampleWithPartialData: IInvoiceItem = {
  id: 22141,
  type: 'AMS',
  description: 'vivid villainous meh',
  quantity: 4527,
  unitPrice: 31916.72,
  priceExtended: 1910.12,
  disRate: 19991.51,
  disAmount: 14681.94,
  subtotal: 24658.48,
  vatRate: 12683.23,
  vat: 2800.74,
  total: 12257.58,
};

export const sampleWithFullData: IInvoiceItem = {
  id: 24613,
  type: 'AMS',
  description: 'youthfully partially true',
  quantity: 8324,
  unitPrice: 3047.04,
  priceExtended: 7454.69,
  disRate: 1007.16,
  disAmount: 22939.5,
  subtotal: 24548.38,
  vatRate: 1735.61,
  vat: 26725.01,
  total: 23102.38,
};

export const sampleWithNewData: NewInvoiceItem = {
  type: 'AMS',
  description: 'quietly about',
  quantity: 20468,
  unitPrice: 29891.92,
  priceExtended: 557.08,
  disRate: 6757.27,
  disAmount: 18275.34,
  subtotal: 13479.24,
  vatRate: 8276.93,
  vat: 7102.89,
  total: 12718.27,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
