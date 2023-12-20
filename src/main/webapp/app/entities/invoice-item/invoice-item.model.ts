import { IInvoice } from 'app/entities/invoice/invoice.model';
import { ProductType } from 'app/entities/enumerations/product-type.model';

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
