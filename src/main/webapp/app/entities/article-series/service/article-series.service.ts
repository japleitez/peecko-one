import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArticleSeries, NewArticleSeries } from '../article-series.model';

export type PartialUpdateArticleSeries = Partial<IArticleSeries> & Pick<IArticleSeries, 'id'>;

type RestOf<T extends IArticleSeries | NewArticleSeries> = Omit<T, 'created' | 'updated' | 'released' | 'archived'> & {
  created?: string | null;
  updated?: string | null;
  released?: string | null;
  archived?: string | null;
};

export type RestArticleSeries = RestOf<IArticleSeries>;

export type NewRestArticleSeries = RestOf<NewArticleSeries>;

export type PartialUpdateRestArticleSeries = RestOf<PartialUpdateArticleSeries>;

export type EntityResponseType = HttpResponse<IArticleSeries>;
export type EntityArrayResponseType = HttpResponse<IArticleSeries[]>;

@Injectable({ providedIn: 'root' })
export class ArticleSeriesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/article-series');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(articleSeries: NewArticleSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleSeries);
    return this.http
      .post<RestArticleSeries>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(articleSeries: IArticleSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleSeries);
    return this.http
      .put<RestArticleSeries>(`${this.resourceUrl}/${this.getArticleSeriesIdentifier(articleSeries)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(articleSeries: PartialUpdateArticleSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(articleSeries);
    return this.http
      .patch<RestArticleSeries>(`${this.resourceUrl}/${this.getArticleSeriesIdentifier(articleSeries)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestArticleSeries>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestArticleSeries[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArticleSeriesIdentifier(articleSeries: Pick<IArticleSeries, 'id'>): number {
    return articleSeries.id;
  }

  compareArticleSeries(o1: Pick<IArticleSeries, 'id'> | null, o2: Pick<IArticleSeries, 'id'> | null): boolean {
    return o1 && o2 ? this.getArticleSeriesIdentifier(o1) === this.getArticleSeriesIdentifier(o2) : o1 === o2;
  }

  addArticleSeriesToCollectionIfMissing<Type extends Pick<IArticleSeries, 'id'>>(
    articleSeriesCollection: Type[],
    ...articleSeriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const articleSeries: Type[] = articleSeriesToCheck.filter(isPresent);
    if (articleSeries.length > 0) {
      const articleSeriesCollectionIdentifiers = articleSeriesCollection.map(
        articleSeriesItem => this.getArticleSeriesIdentifier(articleSeriesItem)!,
      );
      const articleSeriesToAdd = articleSeries.filter(articleSeriesItem => {
        const articleSeriesIdentifier = this.getArticleSeriesIdentifier(articleSeriesItem);
        if (articleSeriesCollectionIdentifiers.includes(articleSeriesIdentifier)) {
          return false;
        }
        articleSeriesCollectionIdentifiers.push(articleSeriesIdentifier);
        return true;
      });
      return [...articleSeriesToAdd, ...articleSeriesCollection];
    }
    return articleSeriesCollection;
  }

  protected convertDateFromClient<T extends IArticleSeries | NewArticleSeries | PartialUpdateArticleSeries>(articleSeries: T): RestOf<T> {
    return {
      ...articleSeries,
      created: articleSeries.created?.toJSON() ?? null,
      updated: articleSeries.updated?.toJSON() ?? null,
      released: articleSeries.released?.toJSON() ?? null,
      archived: articleSeries.archived?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restArticleSeries: RestArticleSeries): IArticleSeries {
    return {
      ...restArticleSeries,
      created: restArticleSeries.created ? dayjs(restArticleSeries.created) : undefined,
      updated: restArticleSeries.updated ? dayjs(restArticleSeries.updated) : undefined,
      released: restArticleSeries.released ? dayjs(restArticleSeries.released) : undefined,
      archived: restArticleSeries.archived ? dayjs(restArticleSeries.archived) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestArticleSeries>): HttpResponse<IArticleSeries> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestArticleSeries[]>): HttpResponse<IArticleSeries[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
