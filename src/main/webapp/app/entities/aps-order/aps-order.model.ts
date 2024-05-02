import { IApsMembership } from 'app/entities/aps-membership/aps-membership.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { IApsPlan } from 'app/entities/aps-plan/aps-plan.model';
import { FieldAccess } from '../../shared/profile/view.models';

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
  period: number | null;
  license: string | null;
  unitPrice?: number | null;
  vatRate?: number | null;
  numberOfUsers?: number | null;
  invoiceNumber?: string | null;
  cstName: string | null;
  cstState: string | null;
  plnId: number;
  plnState: string | null;
  pricing?: string | null;
  contract?: string | null;
}

export interface ApsOrderAccess {
  id: FieldAccess;
  period: FieldAccess;
  license: FieldAccess;
  unitPrice: FieldAccess;
  vatRate: FieldAccess;
  numberOfUsers: FieldAccess;
  invoiceNumber: FieldAccess;
  plan: FieldAccess;
}

export let APS_ORDER_USER_ACCESS: ApsOrderAccess;

APS_ORDER_USER_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  period: { listable: true, visible: true, disabled: false },
  license: { listable: true, visible: true, disabled: false },
  unitPrice: { listable: true, visible: true, disabled: false },
  vatRate: { listable: true, visible: true, disabled: false },
  numberOfUsers: { listable: true, visible: true, disabled: true },
  invoiceNumber: { listable: true, visible: true, disabled: false },
  plan: { listable: true, visible: true, disabled: false },
}
