import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface INotification {
  id: number;
  companyId?: number | null;
  title?: string | null;
  message?: string | null;
  language?: keyof typeof Language | null;
  imageUrl?: string | null;
  videoUrl?: string | null;
  starts?: dayjs.Dayjs | null;
  expires?: dayjs.Dayjs | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };

export interface NotificationAccess {
  id: FieldAccess;
  companyId: FieldAccess;
  title: FieldAccess;
  message: FieldAccess;
  language: FieldAccess;
  imageUrl: FieldAccess;
  videoUrl: FieldAccess;
  starts: FieldAccess;
  expires: FieldAccess;
}

export let NOTIFICATION_ACCESS: NotificationAccess;

NOTIFICATION_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  companyId: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  message: { listable: false, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  imageUrl: { listable: false, visible: true, disabled: false },
  videoUrl: { listable: false, visible: true, disabled: false },
  starts: { listable: true, visible: true, disabled: false },
  expires: { listable: true, visible: true, disabled: false },
};
