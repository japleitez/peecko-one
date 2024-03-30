import dayjs from 'dayjs/esm';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IInvoice {
  id: number;
  number?: string | null;
  issued?: dayjs.Dayjs | null;
  dueDate?: dayjs.Dayjs | null;
  saleDate?: dayjs.Dayjs | null;
  subtotal?: number | null;
  vat?: number | null;
  total?: number | null;
  paid?: number | null;
  paidDate?: dayjs.Dayjs | null;
  diff?: number | null;
  notes?: string | null;
  invoiceItems?: IInvoiceItem[] | null;
  apsOrder?: IApsOrder | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };

export interface InvoiceAccess {
  id: FieldAccess;
  number: FieldAccess;
  issued: FieldAccess;
  dueDate: FieldAccess;
  saleDate: FieldAccess;
  subtotal: FieldAccess;
  vat: FieldAccess;
  total: FieldAccess;
  paid: FieldAccess;
  paidDate: FieldAccess;
  diff: FieldAccess;
  notes: FieldAccess;
  invoiceItems: FieldAccess;
  apsOrder: FieldAccess;
}

export let INVOICE_ACCESS: InvoiceAccess;

INVOICE_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  number: { listable: true, visible: true, disabled: false },
  issued: { listable: false, visible: true, disabled: true },
  dueDate: { listable: true, visible: true, disabled: false },
  saleDate: { listable: true, visible: true, disabled: false },
  subtotal: { listable: true, visible: true, disabled: false },
  vat: { listable: true, visible: true, disabled: false },
  total: { listable: true, visible: true, disabled: false },
  paid: { listable: true, visible: true, disabled: false },
  paidDate: { listable: true, visible: true, disabled: false },
  diff: { listable: true, visible: true, disabled: true },
  notes: { listable: false, visible: true, disabled: false },
  invoiceItems: { listable: false, visible: true, disabled: false },
  apsOrder: { listable: true, visible: true, disabled: false },
};
