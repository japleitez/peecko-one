import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';
import { FieldAccess } from '../../shared/profile/view.models';

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

export interface ArticleSeriesAccess {
  id: FieldAccess;
  code: FieldAccess;
  title: FieldAccess;
  subtitle: FieldAccess;
  summary: FieldAccess;
  language: FieldAccess;
  tags: FieldAccess;
  thumbnail: FieldAccess;
  counter: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  released: FieldAccess;
  archived: FieldAccess;
}

export let ARTICLE_SERIES_ACCESS: ArticleSeriesAccess;

ARTICLE_SERIES_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  subtitle: { listable: false, visible: true, disabled: false },
  summary: { listable: false, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  tags: { listable: false, visible: true, disabled: false },
  thumbnail: { listable: false, visible: true, disabled: false },
  counter: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  updated: { listable: false, visible: true, disabled: true },
  released: { listable: true, visible: true, disabled: false },
  archived: { listable: true, visible: true, disabled: false }
}
