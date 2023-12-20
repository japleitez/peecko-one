import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoach, NewCoach } from '../coach.model';

export type PartialUpdateCoach = Partial<ICoach> & Pick<ICoach, 'id'>;

type RestOf<T extends ICoach | NewCoach> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

export type RestCoach = RestOf<ICoach>;

export type NewRestCoach = RestOf<NewCoach>;

export type PartialUpdateRestCoach = RestOf<PartialUpdateCoach>;

export type EntityResponseType = HttpResponse<ICoach>;
export type EntityArrayResponseType = HttpResponse<ICoach[]>;

@Injectable({ providedIn: 'root' })
export class CoachService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coaches');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(coach: NewCoach): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coach);
    return this.http.post<RestCoach>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(coach: ICoach): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coach);
    return this.http
      .put<RestCoach>(`${this.resourceUrl}/${this.getCoachIdentifier(coach)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(coach: PartialUpdateCoach): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(coach);
    return this.http
      .patch<RestCoach>(`${this.resourceUrl}/${this.getCoachIdentifier(coach)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCoach>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCoach[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCoachIdentifier(coach: Pick<ICoach, 'id'>): number {
    return coach.id;
  }

  compareCoach(o1: Pick<ICoach, 'id'> | null, o2: Pick<ICoach, 'id'> | null): boolean {
    return o1 && o2 ? this.getCoachIdentifier(o1) === this.getCoachIdentifier(o2) : o1 === o2;
  }

  addCoachToCollectionIfMissing<Type extends Pick<ICoach, 'id'>>(
    coachCollection: Type[],
    ...coachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const coaches: Type[] = coachesToCheck.filter(isPresent);
    if (coaches.length > 0) {
      const coachCollectionIdentifiers = coachCollection.map(coachItem => this.getCoachIdentifier(coachItem)!);
      const coachesToAdd = coaches.filter(coachItem => {
        const coachIdentifier = this.getCoachIdentifier(coachItem);
        if (coachCollectionIdentifiers.includes(coachIdentifier)) {
          return false;
        }
        coachCollectionIdentifiers.push(coachIdentifier);
        return true;
      });
      return [...coachesToAdd, ...coachCollection];
    }
    return coachCollection;
  }

  protected convertDateFromClient<T extends ICoach | NewCoach | PartialUpdateCoach>(coach: T): RestOf<T> {
    return {
      ...coach,
      created: coach.created?.toJSON() ?? null,
      updated: coach.updated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCoach: RestCoach): ICoach {
    return {
      ...restCoach,
      created: restCoach.created ? dayjs(restCoach.created) : undefined,
      updated: restCoach.updated ? dayjs(restCoach.updated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCoach>): HttpResponse<ICoach> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCoach[]>): HttpResponse<ICoach[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
