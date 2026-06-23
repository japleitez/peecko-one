import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';
import { ICustomer } from '../customer/customer.model';

export interface INotification {
  id: number;
  title?: string | null;
  message?: string | null;
  language?: keyof typeof Language | null;
  imageUrl?: string | null;
  videoUrl?: string | null;
  starts?: dayjs.Dayjs | null;
  expires?: dayjs.Dayjs | null;
  customer?: ICustomer | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };

export interface NotificationAccess {
  id: FieldAccess;
  title: FieldAccess;
  message: FieldAccess;
  language: FieldAccess;
  imageUrl: FieldAccess;
  videoUrl: FieldAccess;
  starts: FieldAccess;
  expires: FieldAccess;
  customer: FieldAccess;
}

export let NOTIFICATION_ACCESS: NotificationAccess;

NOTIFICATION_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  title: { listable: true, visible: true, disabled: false },
  message: { listable: false, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  imageUrl: { listable: false, visible: true, disabled: false },
  videoUrl: { listable: false, visible: true, disabled: false },
  starts: { listable: true, visible: true, disabled: false },
  expires: { listable: true, visible: true, disabled: false },
  customer: { listable: true, visible: true, disabled: false }
};
