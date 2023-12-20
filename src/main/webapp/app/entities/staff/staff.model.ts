import { IAgency } from 'app/entities/agency/agency.model';

export interface IStaff {
  id: number;
  userId?: number | null;
  role?: string | null;
  agency?: IAgency | null;
}

export type NewStaff = Omit<IStaff, 'id'> & { id: null };
