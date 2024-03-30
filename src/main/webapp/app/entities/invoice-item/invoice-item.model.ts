import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ProductType } from 'app/entities/enumerations/product-type.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IInvoiceItem {
  id: number;
  type?: keyof typeof ProductType | null;
  description?: string | null;
  quantity?: number | null;
  priceUnit?: number | null;
  priceExtended?: number | null;
  disRate?: number | null;
  disAmount?: number | null;
  finalPrice?: number | null;
  vatRate?: number | null;
  vatAmount?: number | null;
  total?: number | null;
  invoice?: IInvoice | null;
}

export type NewInvoiceItem = Omit<IInvoiceItem, 'id'> & { id: null };

export interface InvoiceItemAccess {
  id: FieldAccess;
  type: FieldAccess;
  description: FieldAccess;
  quantity: FieldAccess;
  priceUnit: FieldAccess;
  priceExtended: FieldAccess;
  disRate: FieldAccess;
  disAmount: FieldAccess;
  finalPrice: FieldAccess;
  vatRate: FieldAccess;
  vatAmount: FieldAccess;
  total: FieldAccess;
  invoice: FieldAccess;
}

export let INVOICE_ITEM_ACCESS: InvoiceItemAccess;

INVOICE_ITEM_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  type: { listable: true, visible: true, disabled: false },
  description: { listable: true, visible: true, disabled: false },
  quantity: { listable: true, visible: true, disabled: false },
  priceUnit: { listable: true, visible: true, disabled: false },
  priceExtended: { listable: true, visible: true, disabled: false },
  disRate: { listable: true, visible: true, disabled: false },
  disAmount: { listable: true, visible: true, disabled: false },
  finalPrice: { listable: true, visible: true, disabled: false },
  vatRate: { listable: true, visible: true, disabled: false },
  vatAmount: { listable: true, visible: true, disabled: false },
  total: { listable: true, visible: true, disabled: false },
  invoice: { listable: true, visible: true, disabled: false }
}
