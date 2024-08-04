import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IApsPricing } from 'app/entities/aps-pricing/aps-pricing.model';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IAgency {
  id: number;
  code?: string | null;
  name?: string | null;
  line1?: string | null;
  line2?: string | null;
  zip?: string | null;
  city?: string | null;
  country?: string | null;
  language?: keyof typeof Language | null;
  email?: string | null;
  phone?: string | null;
  billingEmail?: string | null;
  billingPhone?: string | null;
  bank?: string | null;
  iban?: string | null;
  rcs?: string | null;
  vatId?: string | null;
  vatRate?: number | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  customers?: ICustomer[] | null;
  apsPricings?: IApsPricing[] | null;
}

export type NewAgency = Omit<IAgency, 'id'> & { id: null };

export interface AgencyAccess {
  id: FieldAccess;
  code: FieldAccess;
  name: FieldAccess;
  line1: FieldAccess;
  line2: FieldAccess;
  zip: FieldAccess;
  city: FieldAccess;
  country: FieldAccess;
  language: FieldAccess;
  email: FieldAccess;
  phone: FieldAccess;
  billingEmail: FieldAccess;
  billingPhone: FieldAccess;
  bank: FieldAccess;
  iban: FieldAccess;
  rcs: FieldAccess;
  vatId: FieldAccess;
  vatRate: FieldAccess;
  notes: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  customers: FieldAccess;
  apsPricings: FieldAccess;
}

export let AGENCY_USER_ACCESS: AgencyAccess;

AGENCY_USER_ACCESS = {
  apsPricings: { listable: false, visible: true, disabled: false },
  bank: { listable: false, visible: true, disabled: false },
  billingEmail: { listable: true, visible: true, disabled: false },
  billingPhone: { listable: true, visible: true, disabled: false },
  city: { listable: true, visible: true, disabled: false },
  code: { listable: true, visible: true, disabled: false },
  country: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  customers: { listable: false, visible: true, disabled: false },
  email: { listable: true, visible: true, disabled: false },
  iban: { listable: false, visible: true, disabled: false },
  id: { listable: false, visible: true, disabled: true },
  language: { listable: false, visible: true, disabled: false },
  line1: { listable: false, visible: true, disabled: false },
  line2: { listable: false, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  phone: { listable: true, visible: true, disabled: false },
  rcs: { listable: false, visible: true, disabled: false },
  updated: { listable: false, visible: true, disabled: true },
  vatId: { listable: false, visible: true, disabled: false },
  vatRate: { listable: false, visible: true, disabled: false },
  zip: { listable: false, visible: true, disabled: false }
};
