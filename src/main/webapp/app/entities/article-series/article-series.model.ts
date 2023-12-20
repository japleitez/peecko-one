import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';

export interface IArticleSeries {
  id: number;
  code?: string | null;
  title?: string | null;
  subtitle?: string | null;
  summary?: string | null;
  language?: keyof typeof Language | null;
  tags?: string | null;
  thumbnail?: string | null;
  counter?: number | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  released?: dayjs.Dayjs | null;
  archived?: dayjs.Dayjs | null;
}

export type NewArticleSeries = Omit<IArticleSeries, 'id'> & { id: null };
