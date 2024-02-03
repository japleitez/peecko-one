import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IContact {
  id: number;
  type?: keyof typeof ContactType | null;
  name?: string | null;
  line1?: string | null;
  line2?: string | null;
  zip?: string | null;
  city?: string | null;
  country?: string | null;
  email?: string | null;
  phone?: string | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  customer?: ICustomer | null;
}

export type NewContact = Omit<IContact, 'id'> & { id: null };

export interface ContactAccess {
  id: FieldAccess;
  type: FieldAccess;
  name: FieldAccess;
  line1: FieldAccess;
  line2: FieldAccess;
  zip: FieldAccess;
  city: FieldAccess;
  country: FieldAccess;
  email: FieldAccess;
  phone: FieldAccess;
  notes: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  customer: FieldAccess;
}

export let CONTACT_USER_ACCESS: ContactAccess;

CONTACT_USER_ACCESS = {
  city: { listable: true, visible: true, disabled: false },
  country: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  customer: { listable: false, visible: true, disabled: true },
  email: { listable: true, visible: true, disabled: false },
  id: { listable: false, visible: true, disabled: true },
  line1: { listable: true, visible: true, disabled: false },
  line2: { listable: true, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  phone: { listable: true, visible: true, disabled: false },
  type: { listable: true, visible: true, disabled: false },
  updated: { listable: false, visible: true, disabled: true },
  zip: { listable: true, visible: true, disabled: false }
}
