import dayjs from 'dayjs/esm';
import { IArticle } from 'app/entities/article/article.model';
import { FieldAccess } from '../../shared/profile/view.models';

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

export interface ArticleCategoryAccess {
  id: FieldAccess;
  code: FieldAccess;
  title: FieldAccess;
  label: FieldAccess;
  created: FieldAccess;
  release: FieldAccess;
  archived: FieldAccess;
  articles: FieldAccess;
}

export let ARTICLE_CATEGORY_ACCESS: ArticleCategoryAccess;

ARTICLE_CATEGORY_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  label: { listable: true, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  release: { listable: true, visible: true, disabled: false },
  archived: { listable: true, visible: true, disabled: false },
  articles: { listable: false, visible: true, disabled: false },
}
