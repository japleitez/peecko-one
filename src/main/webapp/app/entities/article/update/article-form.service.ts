import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IArticle | NewArticle> = Omit<T, 'created' | 'updated' | 'released' | 'archived'> & {
  created?: string | null;
  updated?: string | null;
  released?: string | null;
  archived?: string | null;
};

type ArticleFormRawValue = FormValueOf<IArticle>;

type NewArticleFormRawValue = FormValueOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id' | 'created' | 'updated' | 'released' | 'archived'>;

type ArticleFormGroupContent = {
  id: FormControl<ArticleFormRawValue['id'] | NewArticle['id']>;
  code: FormControl<ArticleFormRawValue['code']>;
  title: FormControl<ArticleFormRawValue['title']>;
  subtitle: FormControl<ArticleFormRawValue['subtitle']>;
  summary: FormControl<ArticleFormRawValue['summary']>;
  language: FormControl<ArticleFormRawValue['language']>;
  tags: FormControl<ArticleFormRawValue['tags']>;
  duration: FormControl<ArticleFormRawValue['duration']>;
  thumbnail: FormControl<ArticleFormRawValue['thumbnail']>;
  audioUrl: FormControl<ArticleFormRawValue['audioUrl']>;
  content: FormControl<ArticleFormRawValue['content']>;
  seriesId: FormControl<ArticleFormRawValue['seriesId']>;
  chapter: FormControl<ArticleFormRawValue['chapter']>;
  created: FormControl<ArticleFormRawValue['created']>;
  updated: FormControl<ArticleFormRawValue['updated']>;
  released: FormControl<ArticleFormRawValue['released']>;
  archived: FormControl<ArticleFormRawValue['archived']>;
  articleCategory: FormControl<ArticleFormRawValue['articleCategory']>;
  coach: FormControl<ArticleFormRawValue['coach']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = this.convertArticleToArticleRawValue({
      ...this.getFormDefaults(),
      ...article,
    });
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(articleRawValue.code, {
        validators: [Validators.required],
      }),
      title: new FormControl(articleRawValue.title, {
        validators: [Validators.required],
      }),
      subtitle: new FormControl(articleRawValue.subtitle),
      summary: new FormControl(articleRawValue.summary),
      language: new FormControl(articleRawValue.language, {
        validators: [Validators.required],
      }),
      tags: new FormControl(articleRawValue.tags),
      duration: new FormControl(articleRawValue.duration),
      thumbnail: new FormControl(articleRawValue.thumbnail),
      audioUrl: new FormControl(articleRawValue.audioUrl),
      content: new FormControl(articleRawValue.content),
      seriesId: new FormControl(articleRawValue.seriesId),
      chapter: new FormControl(articleRawValue.chapter),
      created: new FormControl(articleRawValue.created),
      updated: new FormControl(articleRawValue.updated),
      released: new FormControl(articleRawValue.released),
      archived: new FormControl(articleRawValue.archived),
      articleCategory: new FormControl(articleRawValue.articleCategory),
      coach: new FormControl(articleRawValue.coach),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return this.convertArticleRawValueToArticle(form.getRawValue() as ArticleFormRawValue | NewArticleFormRawValue);
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = this.convertArticleToArticleRawValue({ ...this.getFormDefaults(), ...article });
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
      released: currentTime,
      archived: currentTime,
    };
  }

  private convertArticleRawValueToArticle(rawArticle: ArticleFormRawValue | NewArticleFormRawValue): IArticle | NewArticle {
    return {
      ...rawArticle,
      created: dayjs(rawArticle.created, DATE_TIME_FORMAT),
      updated: dayjs(rawArticle.updated, DATE_TIME_FORMAT),
      released: dayjs(rawArticle.released, DATE_TIME_FORMAT),
      archived: dayjs(rawArticle.archived, DATE_TIME_FORMAT),
    };
  }

  private convertArticleToArticleRawValue(
    article: IArticle | (Partial<NewArticle> & ArticleFormDefaults),
  ): ArticleFormRawValue | PartialWithRequiredKeyOf<NewArticleFormRawValue> {
    return {
      ...article,
      created: article.created ? article.created.format(DATE_TIME_FORMAT) : undefined,
      updated: article.updated ? article.updated.format(DATE_TIME_FORMAT) : undefined,
      released: article.released ? article.released.format(DATE_TIME_FORMAT) : undefined,
      archived: article.archived ? article.archived.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
