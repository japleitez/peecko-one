import { IAgency } from 'app/entities/agency/agency.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IStaff {
  id: number;
  userId?: number | null;
  role?: string | null;
  agency?: IAgency | null;
}

export type NewStaff = Omit<IStaff, 'id'> & { id: null };

export interface StaffAccess {
  id: FieldAccess;
  userId: FieldAccess;
  role: FieldAccess;
  agency: FieldAccess;
}

export let STAFF_ACCESS: StaffAccess;

STAFF_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  userId: { listable: true, visible: true, disabled: false },
  role: { listable: true, visible: true, disabled: false },
  agency: { listable: true, visible: true, disabled: false },
}
