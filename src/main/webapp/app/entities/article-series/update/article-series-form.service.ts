import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IArticleSeries, NewArticleSeries } from '../article-series.model';

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
  createArticleSeriesFormGroup(articleSeries: ArticleSeriesFormGroupInput = { id: null }): ArticleSeriesFormGroup {
    const articleSeriesRawValue = this.convertArticleSeriesToArticleSeriesRawValue({
      ...this.getFormDefaults(),
      ...articleSeries,
    });
    return new FormGroup<ArticleSeriesFormGroupContent>({
      id: new FormControl(
        { value: articleSeriesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(articleSeriesRawValue.code, {
        validators: [Validators.required],
      }),
      title: new FormControl(articleSeriesRawValue.title, {
        validators: [Validators.required],
      }),
      subtitle: new FormControl(articleSeriesRawValue.subtitle),
      summary: new FormControl(articleSeriesRawValue.summary),
      language: new FormControl(articleSeriesRawValue.language, {
        validators: [Validators.required],
      }),
      tags: new FormControl(articleSeriesRawValue.tags),
      thumbnail: new FormControl(articleSeriesRawValue.thumbnail),
      counter: new FormControl(articleSeriesRawValue.counter, {
        validators: [Validators.required],
      }),
      created: new FormControl(articleSeriesRawValue.created),
      updated: new FormControl(articleSeriesRawValue.updated),
      released: new FormControl(articleSeriesRawValue.released),
      archived: new FormControl(articleSeriesRawValue.archived),
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
