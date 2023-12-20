import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';

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
