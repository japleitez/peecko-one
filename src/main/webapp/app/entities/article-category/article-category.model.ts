import dayjs from 'dayjs/esm';
import { IArticle } from 'app/entities/article/article.model';

export interface IArticleCategory {
  id: number;
  code?: string | null;
  title?: string | null;
  label?: string | null;
  created?: dayjs.Dayjs | null;
  release?: dayjs.Dayjs | null;
  archived?: dayjs.Dayjs | null;
  articles?: IArticle[] | null;
}

export type NewArticleCategory = Omit<IArticleCategory, 'id'> & { id: null };
