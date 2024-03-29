import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ARTICLE_SERIES_ACCESS, ArticleSeriesAccess, IArticleSeries, NewArticleSeries } from '../article-series.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticleSeries for edit and NewArticleSeriesFormGroupInput for create.
 */
type ArticleSeriesFormGroupInput = IArticleSeries | PartialWithRequiredKeyOf<NewArticleSeries>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArticleSeries | NewArticleSeries> = Omit<T, 'created' | 'updated' | 'released' | 'archived'> & {
  created?: string | null;
  updated?: string | null;
  released?: string | null;
  archived?: string | null;
};

type ArticleSeriesFormRawValue = FormValueOf<IArticleSeries>;

type NewArticleSeriesFormRawValue = FormValueOf<NewArticleSeries>;

type ArticleSeriesFormDefaults = Pick<NewArticleSeries, 'id' | 'created' | 'updated' | 'released' | 'archived'>;

type ArticleSeriesFormGroupContent = {
  id: FormControl<ArticleSeriesFormRawValue['id'] | NewArticleSeries['id']>;
  code: FormControl<ArticleSeriesFormRawValue['code']>;
  title: FormControl<ArticleSeriesFormRawValue['title']>;
  subtitle: FormControl<ArticleSeriesFormRawValue['subtitle']>;
  summary: FormControl<ArticleSeriesFormRawValue['summary']>;
  language: FormControl<ArticleSeriesFormRawValue['language']>;
  tags: FormControl<ArticleSeriesFormRawValue['tags']>;
  thumbnail: FormControl<ArticleSeriesFormRawValue['thumbnail']>;
  counter: FormControl<ArticleSeriesFormRawValue['counter']>;
  created: FormControl<ArticleSeriesFormRawValue['created']>;
  updated: FormControl<ArticleSeriesFormRawValue['updated']>;
  released: FormControl<ArticleSeriesFormRawValue['released']>;
  archived: FormControl<ArticleSeriesFormRawValue['archived']>;
};

export type ArticleSeriesFormGroup = FormGroup<ArticleSeriesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleSeriesFormService {
  createArticleSeriesFormGroup(articleSeries: ArticleSeriesFormGroupInput = { id: null }, ua: ArticleSeriesAccess = ARTICLE_SERIES_ACCESS): ArticleSeriesFormGroup {
    const articleSeriesRawValue = this.convertArticleSeriesToArticleSeriesRawValue({
      ...this.getFormDefaults(),
      ...articleSeries,
    });
    return new FormGroup<ArticleSeriesFormGroupContent>({
      id: new FormControl(
        { value: articleSeriesRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl({ value: articleSeriesRawValue.code, disabled: ua.code.disabled },
        { validators: [Validators.required],
      }),
      title: new FormControl({ value: articleSeriesRawValue.title, disabled: ua.title.disabled },
        { validators: [Validators.required],
      }),
      subtitle: new FormControl({ value: articleSeriesRawValue.subtitle, disabled: ua.subtitle.disabled }),
      summary: new FormControl({ value: articleSeriesRawValue.summary, disabled: ua.summary.disabled }),
      language: new FormControl({ value: articleSeriesRawValue.language, disabled: ua.language.disabled },
        { validators: [Validators.required],
      }),
      tags: new FormControl({ value: articleSeriesRawValue.tags, disabled: ua.tags.disabled }),
      thumbnail: new FormControl({ value: articleSeriesRawValue.thumbnail, disabled: ua.thumbnail.disabled }),
      counter: new FormControl({ value: articleSeriesRawValue.counter, disabled: ua.counter.disabled },
        { validators: [Validators.required],
      }),
      created: new FormControl({ value: articleSeriesRawValue.created, disabled: ua.created.disabled }),
      updated: new FormControl({ value: articleSeriesRawValue.updated, disabled: ua.updated.disabled }),
      released: new FormControl({ value: articleSeriesRawValue.released, disabled: ua.released.disabled }),
      archived: new FormControl({ value: articleSeriesRawValue.archived, disabled: ua.archived.disabled }),
    });
  }

  getArticleSeries(form: ArticleSeriesFormGroup): IArticleSeries | NewArticleSeries {
    return this.convertArticleSeriesRawValueToArticleSeries(form.getRawValue() as ArticleSeriesFormRawValue | NewArticleSeriesFormRawValue);
  }

  resetForm(form: ArticleSeriesFormGroup, articleSeries: ArticleSeriesFormGroupInput): void {
    const articleSeriesRawValue = this.convertArticleSeriesToArticleSeriesRawValue({ ...this.getFormDefaults(), ...articleSeries });
    form.reset(
      {
        ...articleSeriesRawValue,
        id: { value: articleSeriesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleSeriesFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
      released: currentTime,
      archived: currentTime,
    };
  }

  private convertArticleSeriesRawValueToArticleSeries(
    rawArticleSeries: ArticleSeriesFormRawValue | NewArticleSeriesFormRawValue,
  ): IArticleSeries | NewArticleSeries {
    return {
      ...rawArticleSeries,
      created: dayjs(rawArticleSeries.created, DATE_TIME_FORMAT),
      updated: dayjs(rawArticleSeries.updated, DATE_TIME_FORMAT),
      released: dayjs(rawArticleSeries.released, DATE_TIME_FORMAT),
      archived: dayjs(rawArticleSeries.archived, DATE_TIME_FORMAT),
    };
  }

  private convertArticleSeriesToArticleSeriesRawValue(
    articleSeries: IArticleSeries | (Partial<NewArticleSeries> & ArticleSeriesFormDefaults),
  ): ArticleSeriesFormRawValue | PartialWithRequiredKeyOf<NewArticleSeriesFormRawValue> {
    return {
      ...articleSeries,
      created: articleSeries.created ? articleSeries.created.format(DATE_TIME_FORMAT) : undefined,
      updated: articleSeries.updated ? articleSeries.updated.format(DATE_TIME_FORMAT) : undefined,
      released: articleSeries.released ? articleSeries.released.format(DATE_TIME_FORMAT) : undefined,
      archived: articleSeries.archived ? articleSeries.archived.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
