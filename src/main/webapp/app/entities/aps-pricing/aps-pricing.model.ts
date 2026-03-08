import { IAgency } from 'app/entities/agency/agency.model';
import { FieldAccess } from '../../shared/profile/view.models';
import { ICustomer } from '../customer/customer.model';

export interface IApsPricing {
  id: number;
  country?: string | null;
  index?: number | null;
  minQuantity?: number | null;
  maxQuantity?: number | null;
  unitPrice?: number | null;
  premiumPrice?: number | null;
  customer?: ICustomer | null;
}

export type NewApsPricing = Omit<IApsPricing, 'id'> & { id: null };

export interface ApsPricingAccess {
  id: FieldAccess;
  country: FieldAccess;
  index: FieldAccess;
  minQuantity: FieldAccess;
  maxQuantity: FieldAccess;
  unitPrice: FieldAccess;
  premiumPrice: FieldAccess;
  customer: FieldAccess;
}

export let APS_PRICING_ACCESS: ApsPricingAccess;

APS_PRICING_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  country: { listable: true, visible: true, disabled: false },
  index: { listable: true, visible: true, disabled: false },
  minQuantity: { listable: true, visible: true, disabled: false },
  maxQuantity: { listable: true, visible: true, disabled: false },
  unitPrice: { listable: true, visible: true, disabled: false },
  premiumPrice: { listable: true, visible: true, disabled: false },
  customer: { listable: true, visible: true, disabled: false },
}
