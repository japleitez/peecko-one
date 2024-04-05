import dayjs from 'dayjs/esm';
import { IArticleCategory } from 'app/entities/article-category/article-category.model';
import { ICoach } from 'app/entities/coach/coach.model';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

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

export interface ArticleAccess {
  id: FieldAccess;
  code: FieldAccess;
  title: FieldAccess;
  subtitle: FieldAccess;
  summary: FieldAccess;
  language: FieldAccess;
  tags: FieldAccess;
  duration: FieldAccess;
  thumbnail: FieldAccess;
  audioUrl: FieldAccess;
  content: FieldAccess;
  seriesId: FieldAccess;
  chapter: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  released: FieldAccess;
  archived: FieldAccess;
  articleCategory: FieldAccess;
  coach: FieldAccess
}

export let ARTICLE_ACCESS: ArticleAccess;

ARTICLE_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  subtitle: { listable: false, visible: true, disabled: false },
  summary: { listable: false, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  tags: { listable: false, visible: true, disabled: false },
  duration: { listable: true, visible: true, disabled: false },
  thumbnail: { listable: false, visible: true, disabled: false },
  audioUrl: { listable: false, visible: true, disabled: false },
  content: { listable: false, visible: true, disabled: false },
  seriesId: { listable: true, visible: true, disabled: false },
  chapter: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  updated: { listable: false, visible: true, disabled: true },
  released: { listable: true, visible: true, disabled: false },
  archived: { listable: true, visible: true, disabled: false },
  articleCategory: { listable: true, visible: true, disabled: false },
  coach: { listable: false, visible: true, disabled: false }
}
