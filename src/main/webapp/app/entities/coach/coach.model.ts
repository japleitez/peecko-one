import dayjs from 'dayjs/esm';
import { IVideo } from 'app/entities/video/video.model';
import { IArticle } from 'app/entities/article/article.model';
import { CoachType } from 'app/entities/enumerations/coach-type.model';

export interface ICoach {
  id: number;
  type?: keyof typeof CoachType | null;
  name?: string | null;
  email?: string | null;
  website?: string | null;
  instagram?: string | null;
  phoneNumber?: string | null;
  country?: string | null;
  speaks?: string | null;
  resume?: string | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  videos?: IVideo[] | null;
  articles?: IArticle[] | null;
}

export type NewCoach = Omit<ICoach, 'id'> & { id: null };
