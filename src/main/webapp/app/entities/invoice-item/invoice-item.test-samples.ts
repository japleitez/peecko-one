import { IInvoiceItem, NewInvoiceItem } from './invoice-item.model';

export const sampleWithRequiredData: IInvoiceItem = {
  id: 18196,
  type: 'APP',
  description: 'correctly like bat',
  quantity: 10920,
  unitPrice: 17209.68,
  subtotal: 28541.42,
};

export const sampleWithPartialData: IInvoiceItem = {
  id: 22141,
  type: 'APP',
  description: 'vivid villainous meh',
  quantity: 4527,
  unitPrice: 31916.72,
  subtotal: 24658.48,
};

export const sampleWithFullData: IInvoiceItem = {
  id: 24613,
  type: 'SRV',
  description: 'youthfully partially true',
  quantity: 8324,
  unitPrice: 3047.04,
  subtotal: 24548.38,
};

export const sampleWithNewData: NewInvoiceItem = {
  type: 'APP',
  description: 'quietly about',
  quantity: 20468,
  unitPrice: 29891.92,
  subtotal: 13479.24,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
