import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArticleCategory, NewArticleCategory } from '../article-category.model';

export type PartialUpdateArticleCategory = Partial<IArticleCategory> & Pick<IArticleCategory, 'id'>;

type RestOf<T extends IArticleCategory | NewArticleCategory> = Omit<T, 'created' | 'release' | 'archived'> & {
  created?: string | null;
  release?: string | null;
  archived?: string | null;
};

export type RestArticleCategory = RestOf<IArticleCategory>;

export type NewRestArticleCategory = RestOf<NewArticleCategory>;

export type PartialUpdateRestArticleCategory = RestOf<PartialUpdateArticleCategory>;

export type EntityResponseType = HttpResponse<IArticleCategory>;
export type EntityArrayResponseType = HttpResponse<IArticleCategory[]>;

@Injectable({ providedIn: 'root' })
export class ArticleCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/article-categories');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(articleCategory: NewArticleCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleCategory);
    return this.http
      .post<RestArticleCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(articleCategory: IArticleCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleCategory);
    return this.http
      .put<RestArticleCategory>(`${this.resourceUrl}/${this.getArticleCategoryIdentifier(articleCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(articleCategory: PartialUpdateArticleCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleCategory);
    return this.http
      .patch<RestArticleCategory>(`${this.resourceUrl}/${this.getArticleCategoryIdentifier(articleCategory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestArticleCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestArticleCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArticleCategoryIdentifier(articleCategory: Pick<IArticleCategory, 'id'>): number {
    return articleCategory.id;
  }

  compareArticleCategory(o1: Pick<IArticleCategory, 'id'> | null, o2: Pick<IArticleCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getArticleCategoryIdentifier(o1) === this.getArticleCategoryIdentifier(o2) : o1 === o2;
  }

  addArticleCategoryToCollectionIfMissing<Type extends Pick<IArticleCategory, 'id'>>(
    articleCategoryCollection: Type[],
    ...articleCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const articleCategories: Type[] = articleCategoriesToCheck.filter(isPresent);
    if (articleCategories.length > 0) {
      const articleCategoryCollectionIdentifiers = articleCategoryCollection.map(
        articleCategoryItem => this.getArticleCategoryIdentifier(articleCategoryItem)!,
      );
      const articleCategoriesToAdd = articleCategories.filter(articleCategoryItem => {
        const articleCategoryIdentifier = this.getArticleCategoryIdentifier(articleCategoryItem);
        if (articleCategoryCollectionIdentifiers.includes(articleCategoryIdentifier)) {
          return false;
        }
        articleCategoryCollectionIdentifiers.push(articleCategoryIdentifier);
        return true;
      });
      return [...articleCategoriesToAdd, ...articleCategoryCollection];
    }
    return articleCategoryCollection;
  }

  protected convertDateFromClient<T extends IArticleCategory | NewArticleCategory | PartialUpdateArticleCategory>(
    articleCategory: T,
  ): RestOf<T> {
    return {
      ...articleCategory,
      created: articleCategory.created?.toJSON() ?? null,
      release: articleCategory.release?.toJSON() ?? null,
      archived: articleCategory.archived?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restArticleCategory: RestArticleCategory): IArticleCategory {
    return {
      ...restArticleCategory,
      created: restArticleCategory.created ? dayjs(restArticleCategory.created) : undefined,
      release: restArticleCategory.release ? dayjs(restArticleCategory.release) : undefined,
      archived: restArticleCategory.archived ? dayjs(restArticleCategory.archived) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestArticleCategory>): HttpResponse<IArticleCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestArticleCategory[]>): HttpResponse<IArticleCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
