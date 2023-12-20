import dayjs from 'dayjs/esm';
import { IArticleCategory } from 'app/entities/article-category/article-category.model';
import { ICoach } from 'app/entities/coach/coach.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IArticle {
  id: number;
  code?: string | null;
  title?: string | null;
  subtitle?: string | null;
  summary?: string | null;
  language?: keyof typeof Language | null;
  tags?: string | null;
  duration?: number | null;
  thumbnail?: string | null;
  audioUrl?: string | null;
  content?: string | null;
  seriesId?: number | null;
  chapter?: number | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  released?: dayjs.Dayjs | null;
  archived?: dayjs.Dayjs | null;
  articleCategory?: IArticleCategory | null;
  coach?: ICoach | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
