import dayjs from 'dayjs/esm';
import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { PricingType } from 'app/entities/enumerations/pricing-type.model';
import { PlanState } from 'app/entities/enumerations/plan-state.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IApsPlan {
  id: number;
  contract?: string | null;
  pricing?: keyof typeof PricingType | null;
  state?: keyof typeof PlanState | null;
  license?: string | null;
  starts?: dayjs.Dayjs | null;
  ends?: dayjs.Dayjs | null;
  trialStarts?: dayjs.Dayjs | null;
  trialEnds?: dayjs.Dayjs | null;
  unitPrice?: number | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  apsOrders?: IApsOrder[] | null;
  customer?: ICustomer | null;
}

export type NewApsPlan = Omit<IApsPlan, 'id'> & { id: null };

export interface ApsPlanAccess {
  id: FieldAccess;
  contract: FieldAccess;
  pricing: FieldAccess;
  state: FieldAccess;
  license: FieldAccess;
  starts: FieldAccess;
  ends: FieldAccess;
  trialStarts: FieldAccess;
  trialEnds: FieldAccess;
  unitPrice: FieldAccess;
  notes: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  apsOrders: FieldAccess;
  customer: FieldAccess;
}

export let APS_PLAN_USER_ACCESS: ApsPlanAccess;

APS_PLAN_USER_ACCESS = {
  apsOrders: { listable: true, visible: true, disabled: false },
  contract: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: false },
  customer: { listable: false, visible: true, disabled: true },
  ends: { listable: true, visible: true, disabled: false },
  id: { listable: true, visible: true, disabled: false },
  license: { listable: true, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  pricing: { listable: true, visible: true, disabled: false },
  starts: { listable: true, visible: true, disabled: false },
  state: { listable: true, visible: true, disabled: false },
  trialEnds: { listable: true, visible: true, disabled: false },
  trialStarts: { listable: true, visible: true, disabled: false },
  unitPrice: { listable: true, visible: true, disabled: false },
  updated: { listable: false, visible: true, disabled: false }
}
