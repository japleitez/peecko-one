import { IApsOrder } from 'app/entities/aps-order/aps-order.model';

export interface IApsMembership {
  id: number;
  period?: number | null;
  license?: string | null;
  username?: string | null;
  apsOrder?: IApsOrder | null;
}

export type NewApsMembership = Omit<IApsMembership, 'id'> & { id: null };
