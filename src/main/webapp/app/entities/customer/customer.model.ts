import dayjs from 'dayjs/esm';
import { IContact } from 'app/entities/contact/contact.model';
import { IApsPlan } from 'app/entities/aps-plan/aps-plan.model';
import { IAgency } from 'app/entities/agency/agency.model';
import { CustomerState } from 'app/entities/enumerations/customer-state.model';
import { IFieldAccess } from '../../shared/profile/view.models';

export interface ICustomer {
  id: number;
  code?: string | null;
  name?: string | null;
  country?: string | null;
  license?: string | null;
  state?: keyof typeof CustomerState | null;
  closeReason?: string | null;
  emailDomains?: string | null;
  vatId?: string | null;
  bank?: string | null;
  iban?: string | null;
  logo?: string | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  trialed?: dayjs.Dayjs | null;
  declined?: dayjs.Dayjs | null;
  activated?: dayjs.Dayjs | null;
  closed?: dayjs.Dayjs | null;
  contacts?: IContact[] | null;
  apsPlans?: IApsPlan[] | null;
  agency?: IAgency | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };

export interface CustomerAccess {
  id: IFieldAccess;
  code: IFieldAccess;
  name: IFieldAccess;
  country: IFieldAccess;
  license: IFieldAccess;
  state: IFieldAccess;
  closeReason: IFieldAccess;
  emailDomains: IFieldAccess;
  vatId: IFieldAccess;
  bank: IFieldAccess;
  iban: IFieldAccess;
  logo: IFieldAccess;
  notes: IFieldAccess;
  created: IFieldAccess;
  updated: IFieldAccess;
  trialed: IFieldAccess;
  declined: IFieldAccess;
  activated: IFieldAccess;
  closed: IFieldAccess;
  contacts: IFieldAccess;
  apsPlans: IFieldAccess;
  agency: IFieldAccess;
}

export let CUSTOMER_USER_ACCESS: CustomerAccess;

CUSTOMER_USER_ACCESS = {
  activated: { listable: true, visible: true, disabled: true },
  agency: { listable: false, visible: true, disabled: true },
  apsPlans: { listable: false, visible: true, disabled: false },
  bank: { listable: false, visible: true, disabled: false },
  closeReason: { listable: false, visible: true, disabled: false },
  closed: { listable: true, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  contacts: { listable: false, visible: true, disabled: false },
  country: { listable: false, visible: true, disabled: false },
  created: { listable: true, visible: true, disabled: true },
  declined: { listable: false, visible: true, disabled: true },
  emailDomains: { listable: false, visible: true, disabled: false },
  iban: { listable: false, visible: true, disabled: false },
  id: { listable: false, visible: true, disabled: false },
  license: { listable: true, visible: true, disabled: false },
  logo: { listable: false, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  state: { listable: true, visible: true, disabled: false },
  trialed: { listable: false, visible: true, disabled: true },
  updated: { listable: false, visible: true, disabled: true },
  vatId: { listable: true, visible: true, disabled: false }
};
