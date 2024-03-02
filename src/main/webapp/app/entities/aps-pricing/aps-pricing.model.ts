import { IAgency } from 'app/entities/agency/agency.model';

export interface IApsPricing {
  id: number;
  country?: string | null;
  customerId?: number | null;
  index?: number | null;
  minQuantity?: number | null;
  unitPrice?: number | null;
  agency?: IAgency | null;
}

export type NewApsPricing = Omit<IApsPricing, 'id'> & { id: null };
