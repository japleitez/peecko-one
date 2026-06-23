import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVideoCategory, NewVideoCategory } from '../video-category.model';

export type PartialUpdateVideoCategory = Partial<IVideoCategory> & Pick<IVideoCategory, 'id'>;

type RestOf<T extends IVideoCategory | NewVideoCategory> = Omit<T, 'created' | 'released' | 'archived'> & {
  created?: string | null;
  released?: string | null;
  archived?: string | null;
};

export type RestVideoCategory = RestOf<IVideoCategory>;

export type NewRestVideoCategory = RestOf<NewVideoCategory>;

export type PartialUpdateRestVideoCategory = RestOf<PartialUpdateVideoCategory>;

export type VideoCategoryResponseType = HttpResponse<IVideoCategory>;
export type VideoCategoryArrayResponseType = HttpResponse<IVideoCategory[]>;

@Injectable({ providedIn: 'root' })
export class VideoCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/video-categories');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(videoCategory: NewVideoCategory): Observable<VideoCategoryResponseType> {
    const copy = this.convertDateFromClient(videoCategory);
    return this.http
      .post<RestVideoCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(videoCategory: IVideoCategory): Observable<VideoCategoryResponseType> {
    const copy = this.convertDateFromClient(videoCategory);
    return this.http
      .put<RestVideoCategory>(`${this.resourceUrl}/${this.getVideoCategoryIdentifier(videoCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(videoCategory: PartialUpdateVideoCategory): Observable<VideoCategoryResponseType> {
    const copy = this.convertDateFromClient(videoCategory);
    return this.http
      .patch<RestVideoCategory>(`${this.resourceUrl}/${this.getVideoCategoryIdentifier(videoCategory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<VideoCategoryResponseType> {
    return this.http
      .get<RestVideoCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<VideoCategoryArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVideoCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVideoCategoryIdentifier(videoCategory: Pick<IVideoCategory, 'id'>): number {
    return videoCategory.id;
  }

  compareVideoCategory(o1: Pick<IVideoCategory, 'id'> | null, o2: Pick<IVideoCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getVideoCategoryIdentifier(o1) === this.getVideoCategoryIdentifier(o2) : o1 === o2;
  }

  addVideoCategoryToCollectionIfMissing<Type extends Pick<IVideoCategory, 'id'>>(
    videoCategoryCollection: Type[],
    ...videoCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const videoCategories: Type[] = videoCategoriesToCheck.filter(isPresent);
    if (videoCategories.length > 0) {
      const videoCategoryCollectionIdentifiers = videoCategoryCollection.map(
        videoCategoryItem => this.getVideoCategoryIdentifier(videoCategoryItem)!,
      );
      const videoCategoriesToAdd = videoCategories.filter(videoCategoryItem => {
        const videoCategoryIdentifier = this.getVideoCategoryIdentifier(videoCategoryItem);
        if (videoCategoryCollectionIdentifiers.includes(videoCategoryIdentifier)) {
          return false;
        }
        videoCategoryCollectionIdentifiers.push(videoCategoryIdentifier);
        return true;
      });
      return [...videoCategoriesToAdd, ...videoCategoryCollection];
    }
    return videoCategoryCollection;
  }

  protected convertDateFromClient<T extends IVideoCategory | NewVideoCategory | PartialUpdateVideoCategory>(videoCategory: T): RestOf<T> {
    return {
      ...videoCategory,
      created: videoCategory.created?.toJSON() ?? null,
      released: videoCategory.released?.toJSON() ?? null,
      archived: videoCategory.archived?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVideoCategory: RestVideoCategory): IVideoCategory {
    return {
      ...restVideoCategory,
      created: restVideoCategory.created ? dayjs(restVideoCategory.created) : undefined,
      released: restVideoCategory.released ? dayjs(restVideoCategory.released) : undefined,
      archived: restVideoCategory.archived ? dayjs(restVideoCategory.archived) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVideoCategory>): HttpResponse<IVideoCategory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVideoCategory[]>): HttpResponse<IVideoCategory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
