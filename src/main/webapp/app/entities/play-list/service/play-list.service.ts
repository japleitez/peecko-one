import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayList, NewPlayList } from '../play-list.model';

export type PartialUpdatePlayList = Partial<IPlayList> & Pick<IPlayList, 'id'>;

type RestOf<T extends IPlayList | NewPlayList> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

export type RestPlayList = RestOf<IPlayList>;

export type NewRestPlayList = RestOf<NewPlayList>;

export type PartialUpdateRestPlayList = RestOf<PartialUpdatePlayList>;

export type EntityResponseType = HttpResponse<IPlayList>;
export type EntityArrayResponseType = HttpResponse<IPlayList[]>;

@Injectable({ providedIn: 'root' })
export class PlayListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/play-lists');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(playList: NewPlayList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playList);
    return this.http
      .post<RestPlayList>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playList: IPlayList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playList);
    return this.http
      .put<RestPlayList>(`${this.resourceUrl}/${this.getPlayListIdentifier(playList)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playList: PartialUpdatePlayList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playList);
    return this.http
      .patch<RestPlayList>(`${this.resourceUrl}/${this.getPlayListIdentifier(playList)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlayList>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlayList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlayListIdentifier(playList: Pick<IPlayList, 'id'>): number {
    return playList.id;
  }

  comparePlayList(o1: Pick<IPlayList, 'id'> | null, o2: Pick<IPlayList, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlayListIdentifier(o1) === this.getPlayListIdentifier(o2) : o1 === o2;
  }

  addPlayListToCollectionIfMissing<Type extends Pick<IPlayList, 'id'>>(
    playListCollection: Type[],
    ...playListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playLists: Type[] = playListsToCheck.filter(isPresent);
    if (playLists.length > 0) {
      const playListCollectionIdentifiers = playListCollection.map(playListItem => this.getPlayListIdentifier(playListItem)!);
      const playListsToAdd = playLists.filter(playListItem => {
        const playListIdentifier = this.getPlayListIdentifier(playListItem);
        if (playListCollectionIdentifiers.includes(playListIdentifier)) {
          return false;
        }
        playListCollectionIdentifiers.push(playListIdentifier);
        return true;
      });
      return [...playListsToAdd, ...playListCollection];
    }
    return playListCollection;
  }

  protected convertDateFromClient<T extends IPlayList | NewPlayList | PartialUpdatePlayList>(playList: T): RestOf<T> {
    return {
      ...playList,
      created: playList.created?.toJSON() ?? null,
      updated: playList.updated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlayList: RestPlayList): IPlayList {
    return {
      ...restPlayList,
      created: restPlayList.created ? dayjs(restPlayList.created) : undefined,
      updated: restPlayList.updated ? dayjs(restPlayList.updated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlayList>): HttpResponse<IPlayList> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlayList[]>): HttpResponse<IPlayList[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
