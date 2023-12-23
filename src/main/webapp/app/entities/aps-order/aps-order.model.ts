import { IApsMembership } from 'app/entities/aps-membership/aps-membership.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IApsPlan } from 'app/entities/aps-plan/aps-plan.model';

export interface IApsOrder {
  id: number;
  period?: number | null;
  license?: string | null;
  unitPrice?: number | null;
  vatRate?: number | null;
  numberOfUsers?: number | null;
  invoiceNumber?: string | null;
  apsMemberships?: IApsMembership[] | null;
  invoices?: IInvoice[] | null;
  apsPlan?: IApsPlan | null;
}

export type NewApsOrder = Omit<IApsOrder, 'id'> & { id: null };

export interface IApsOrderInfo {
  id: number;
  period?: number | null;
  license?: string | null;
  unitPrice?: number | null;
  vatRate?: number | null;
  numberOfUsers?: number | null;
  invoiceNumber?: string | null;
  cstName?: string | null;
  cstState?: string | null;
  plnId: number;
  plnState?: string | null;
  pricing?: string | null;
  contract?: string | null;
}
