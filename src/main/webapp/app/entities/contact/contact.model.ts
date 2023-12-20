import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ContactType } from 'app/entities/enumerations/contact-type.model';

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
