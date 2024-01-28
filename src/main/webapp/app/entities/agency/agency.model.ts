import dayjs from 'dayjs/esm';
import { IStaff } from 'app/entities/staff/staff.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IApsPricing } from 'app/entities/aps-pricing/aps-pricing.model';
import { Language } from 'app/entities/enumerations/language.model';
import { IFieldAccess } from '../../shared/profile/view.models';

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
  staff?: IStaff[] | null;
  customers?: ICustomer[] | null;
  apsPricings?: IApsPricing[] | null;
}

export type NewAgency = Omit<IAgency, 'id'> & { id: null };

export interface AgencyAccess {
  id: IFieldAccess;
  code: IFieldAccess;
  name: IFieldAccess;
  line1: IFieldAccess;
  line2: IFieldAccess;
  zip: IFieldAccess;
  city: IFieldAccess;
  country: IFieldAccess;
  language: IFieldAccess;
  email: IFieldAccess;
  phone: IFieldAccess;
  billingEmail: IFieldAccess;
  billingPhone: IFieldAccess;
  bank: IFieldAccess;
  iban: IFieldAccess;
  rcs: IFieldAccess;
  vatId: IFieldAccess;
  vatRate: IFieldAccess;
  notes: IFieldAccess;
  created: IFieldAccess;
  updated: IFieldAccess;
  staff: IFieldAccess;
  customers: IFieldAccess;
  apsPricings: IFieldAccess;
}

export let AGENCY_USER_ACCESS: AgencyAccess;
AGENCY_USER_ACCESS = {
  apsPricings: { listable: false, visible: true, disabled: false },
  bank: { listable: false, visible: true, disabled: false },
  billingEmail: { listable: false, visible: true, disabled: false },
  billingPhone: { listable: true, visible: true, disabled: false },
  city: { listable: true, visible: true, disabled: false },
  code: { listable: true, visible: true, disabled: false },
  country: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  customers: { listable: false, visible: true, disabled: false },
  email: { listable: true, visible: true, disabled: false },
  iban: { listable: false, visible: true, disabled: false },
  id: { listable: true, visible: true, disabled: true },
  language: { listable: false, visible: true, disabled: false },
  line1: { listable: false, visible: true, disabled: false },
  line2: { listable: false, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  phone: { listable: true, visible: true, disabled: false },
  rcs: { listable: false, visible: true, disabled: false },
  staff: { listable: false, visible: true, disabled: false },
  updated: { listable: false, visible: true, disabled: true },
  vatId: { listable: false, visible: true, disabled: false },
  vatRate: { listable: true, visible: true, disabled: false },
  zip: { listable: false, visible: true, disabled: false }
};
