import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ProductType } from 'app/entities/enumerations/product-type.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IInvoiceItem {
  id: number;
  type?: keyof typeof ProductType | null;
  description?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  subtotal?: number | null;
  vatRate?: number | null;
  vat?: number | null;
  total?: number | null;
  invoice?: IInvoice | null;
}

export type NewInvoiceItem = Omit<IInvoiceItem, 'id'> & { id: null };

export interface InvoiceItemAccess {
  id: FieldAccess;
  type: FieldAccess;
  description: FieldAccess;
  quantity: FieldAccess;
  unitPrice: FieldAccess;
  subtotal: FieldAccess;
  vatRate: FieldAccess;
  vat: FieldAccess;
  total: FieldAccess;
  invoice: FieldAccess;
}

export let INVOICE_ITEM_ACCESS: InvoiceItemAccess;

INVOICE_ITEM_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  type: { listable: true, visible: true, disabled: false },
  description: { listable: true, visible: true, disabled: false },
  quantity: { listable: true, visible: true, disabled: false },
  unitPrice: { listable: true, visible: true, disabled: false },
  subtotal: { listable: true, visible: true, disabled: false },
  vatRate: { listable: true, visible: true, disabled: false },
  vat: { listable: true, visible: true, disabled: false },
  total: { listable: true, visible: true, disabled: false },
  invoice: { listable: true, visible: true, disabled: false }
}
