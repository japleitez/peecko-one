import dayjs from 'dayjs/esm';
import { FieldAccess } from '../../shared/profile/view.models';
import { ICustomer } from '../customer/customer.model';

export interface INotification {
  id: number;
  title?: string | null;
  message?: string | null;
  language?: string | null;
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

export const NOTIFICATION_ACCESS: NotificationAccess = {
  id: { listable: false, visible: true, disabled: true },
  title: { listable: true, visible: true, disabled: false },
  message: { listable: false, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  imageUrl: { listable: false, visible: true, disabled: false },
  videoUrl: { listable: false, visible: true, disabled: false },
  starts: { listable: true, visible: true, disabled: false },
  expires: { listable: true, visible: true, disabled: false },
  customer: { listable: true, visible: true, disabled: false },
};
