import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import {
  ARTICLE_CATEGORY_ACCESS,
  ArticleCategoryAccess,
  IArticleCategory,
  NewArticleCategory
} from '../article-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticleCategory for edit and NewArticleCategoryFormGroupInput for create.
 */
type ArticleCategoryFormGroupInput = IArticleCategory | PartialWithRequiredKeyOf<NewArticleCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArticleCategory | NewArticleCategory> = Omit<T, 'created' | 'release' | 'archived'> & {
  created?: string | null;
  release?: string | null;
  archived?: string | null;
};

type ArticleCategoryFormRawValue = FormValueOf<IArticleCategory>;

type NewArticleCategoryFormRawValue = FormValueOf<NewArticleCategory>;

type ArticleCategoryFormDefaults = Pick<NewArticleCategory, 'id' | 'created' | 'release' | 'archived'>;

type ArticleCategoryFormGroupContent = {
  id: FormControl<ArticleCategoryFormRawValue['id'] | NewArticleCategory['id']>;
  code: FormControl<ArticleCategoryFormRawValue['code']>;
  title: FormControl<ArticleCategoryFormRawValue['title']>;
  label: FormControl<ArticleCategoryFormRawValue['label']>;
  created: FormControl<ArticleCategoryFormRawValue['created']>;
  release: FormControl<ArticleCategoryFormRawValue['release']>;
  archived: FormControl<ArticleCategoryFormRawValue['archived']>;
};

export type ArticleCategoryFormGroup = FormGroup<ArticleCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleCategoryFormService {
  createArticleCategoryFormGroup(articleCategory: ArticleCategoryFormGroupInput = { id: null }, ua: ArticleCategoryAccess =  ARTICLE_CATEGORY_ACCESS): ArticleCategoryFormGroup {
    const articleCategoryRawValue = this.convertArticleCategoryToArticleCategoryRawValue({
      ...this.getFormDefaults(),
      ...articleCategory,
    });
    return new FormGroup<ArticleCategoryFormGroupContent>({
      id: new FormControl(
        { value: articleCategoryRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl({ value: articleCategoryRawValue.code, disabled: ua.code.disabled },
        { validators: [Validators.required],
      }),
      title: new FormControl({ value: articleCategoryRawValue.title, disabled: ua.title.disabled },
        { validators: [Validators.required],
      }),
      label: new FormControl({ value: articleCategoryRawValue.label, disabled: ua.label.disabled },
        { validators: [Validators.required],
      }),
      created: new FormControl({ value: articleCategoryRawValue.created, disabled: ua.created.disabled }),
      release: new FormControl({ value: articleCategoryRawValue.release, disabled: ua.release.disabled }),
      archived: new FormControl({ value: articleCategoryRawValue.archived, disabled: ua.archived.disabled }),
    });
  }

  getArticleCategory(form: ArticleCategoryFormGroup): IArticleCategory | NewArticleCategory {
    return this.convertArticleCategoryRawValueToArticleCategory(
      form.getRawValue() as ArticleCategoryFormRawValue | NewArticleCategoryFormRawValue,
    );
  }

  resetForm(form: ArticleCategoryFormGroup, articleCategory: ArticleCategoryFormGroupInput): void {
    const articleCategoryRawValue = this.convertArticleCategoryToArticleCategoryRawValue({ ...this.getFormDefaults(), ...articleCategory });
    form.reset(
      {
        ...articleCategoryRawValue,
        id: { value: articleCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      release: null,
      archived: null,
    };
  }

  private convertArticleCategoryRawValueToArticleCategory(
    rawArticleCategory: ArticleCategoryFormRawValue | NewArticleCategoryFormRawValue,
  ): IArticleCategory | NewArticleCategory {
    return {
      ...rawArticleCategory,
      created: dayjs(rawArticleCategory.created, DATE_TIME_FORMAT),
      release: dayjs(rawArticleCategory.release, DATE_TIME_FORMAT),
      archived: dayjs(rawArticleCategory.archived, DATE_TIME_FORMAT),
    };
  }

  private convertArticleCategoryToArticleCategoryRawValue(
    articleCategory: IArticleCategory | (Partial<NewArticleCategory> & ArticleCategoryFormDefaults),
  ): ArticleCategoryFormRawValue | PartialWithRequiredKeyOf<NewArticleCategoryFormRawValue> {
    return {
      ...articleCategory,
      created: articleCategory.created ? articleCategory.created.format(DATE_TIME_FORMAT) : undefined,
      release: articleCategory.release ? articleCategory.release.format(DATE_TIME_FORMAT) : undefined,
      archived: articleCategory.archived ? articleCategory.archived.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
