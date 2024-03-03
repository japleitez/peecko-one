import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IApsMembership {
  id: number;
  period?: number | null;
  license?: string | null;
  username?: string | null;
  apsOrder?: IApsOrder | null;
}

export type NewApsMembership = Omit<IApsMembership, 'id'> & { id: null };

export interface ApsMembershipAccess {
  id: FieldAccess;
  period: FieldAccess;
  license: FieldAccess;
  username: FieldAccess;
  apsOrder: FieldAccess;
}

export let APS_MEMBERSHIP_USER_ACCESS: ApsMembershipAccess;

APS_MEMBERSHIP_USER_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  period: { listable: true, visible: true, disabled: false },
  license: { listable: true, visible: true, disabled: false },
  username: { listable: true, visible: true, disabled: false },
  apsOrder: { listable: false, visible: true, disabled: false }
}
