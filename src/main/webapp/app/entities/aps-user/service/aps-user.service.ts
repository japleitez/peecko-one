import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsUser, NewApsUser } from '../aps-user.model';

export type PartialUpdateApsUser = Partial<IApsUser> & Pick<IApsUser, 'id'>;

type RestOf<T extends IApsUser | NewApsUser> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

export type RestApsUser = RestOf<IApsUser>;

export type NewRestApsUser = RestOf<NewApsUser>;

export type PartialUpdateRestApsUser = RestOf<PartialUpdateApsUser>;

export type EntityResponseType = HttpResponse<IApsUser>;
export type EntityArrayResponseType = HttpResponse<IApsUser[]>;

@Injectable({ providedIn: 'root' })
export class ApsUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-users');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsUser: NewApsUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsUser);
    return this.http
      .post<RestApsUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(apsUser: IApsUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsUser);
    return this.http
      .put<RestApsUser>(`${this.resourceUrl}/${this.getApsUserIdentifier(apsUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(apsUser: PartialUpdateApsUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsUser);
    return this.http
      .patch<RestApsUser>(`${this.resourceUrl}/${this.getApsUserIdentifier(apsUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestApsUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestApsUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsUserIdentifier(apsUser: Pick<IApsUser, 'id'>): number {
    return apsUser.id;
  }

  compareApsUser(o1: Pick<IApsUser, 'id'> | null, o2: Pick<IApsUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsUserIdentifier(o1) === this.getApsUserIdentifier(o2) : o1 === o2;
  }

  addApsUserToCollectionIfMissing<Type extends Pick<IApsUser, 'id'>>(
    apsUserCollection: Type[],
    ...apsUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsUsers: Type[] = apsUsersToCheck.filter(isPresent);
    if (apsUsers.length > 0) {
      const apsUserCollectionIdentifiers = apsUserCollection.map(apsUserItem => this.getApsUserIdentifier(apsUserItem)!);
      const apsUsersToAdd = apsUsers.filter(apsUserItem => {
        const apsUserIdentifier = this.getApsUserIdentifier(apsUserItem);
        if (apsUserCollectionIdentifiers.includes(apsUserIdentifier)) {
          return false;
        }
        apsUserCollectionIdentifiers.push(apsUserIdentifier);
        return true;
      });
      return [...apsUsersToAdd, ...apsUserCollection];
    }
    return apsUserCollection;
  }

  protected convertDateFromClient<T extends IApsUser | NewApsUser | PartialUpdateApsUser>(apsUser: T): RestOf<T> {
    return {
      ...apsUser,
      created: apsUser.created?.toJSON() ?? null,
      updated: apsUser.updated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restApsUser: RestApsUser): IApsUser {
    return {
      ...restApsUser,
      created: restApsUser.created ? dayjs(restApsUser.created) : undefined,
      updated: restApsUser.updated ? dayjs(restApsUser.updated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestApsUser>): HttpResponse<IApsUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestApsUser[]>): HttpResponse<IApsUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
