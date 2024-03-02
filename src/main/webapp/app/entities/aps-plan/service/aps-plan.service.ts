import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsPlan, NewApsPlan } from '../aps-plan.model';

export type PartialUpdateApsPlan = Partial<IApsPlan> & Pick<IApsPlan, 'id'>;

type RestOf<T extends IApsPlan | NewApsPlan> = Omit<T, 'starts' | 'ends' | 'trialStarts' | 'trialEnds' | 'created' | 'updated'> & {
  starts?: string | null;
  ends?: string | null;
  trialStarts?: string | null;
  trialEnds?: string | null;
  created?: string | null;
  updated?: string | null;
};

export type RestApsPlan = RestOf<IApsPlan>;

export type NewRestApsPlan = RestOf<NewApsPlan>;

export type PartialUpdateRestApsPlan = RestOf<PartialUpdateApsPlan>;

export type EntityResponseType = HttpResponse<IApsPlan>;
export type EntityArrayResponseType = HttpResponse<IApsPlan[]>;

@Injectable({ providedIn: 'root' })
export class ApsPlanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-plans');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsPlan: NewApsPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsPlan);
    return this.http
      .post<RestApsPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(apsPlan: IApsPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsPlan);
    return this.http
      .put<RestApsPlan>(`${this.resourceUrl}/${this.getApsPlanIdentifier(apsPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(apsPlan: PartialUpdateApsPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsPlan);
    return this.http
      .patch<RestApsPlan>(`${this.resourceUrl}/${this.getApsPlanIdentifier(apsPlan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestApsPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestApsPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsPlanIdentifier(apsPlan: Pick<IApsPlan, 'id'>): number {
    return apsPlan.id;
  }

  compareApsPlan(o1: Pick<IApsPlan, 'id'> | null, o2: Pick<IApsPlan, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsPlanIdentifier(o1) === this.getApsPlanIdentifier(o2) : o1 === o2;
  }

  addApsPlanToCollectionIfMissing<Type extends Pick<IApsPlan, 'id'>>(
    apsPlanCollection: Type[],
    ...apsPlansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsPlans: Type[] = apsPlansToCheck.filter(isPresent);
    if (apsPlans.length > 0) {
      const apsPlanCollectionIdentifiers = apsPlanCollection.map(apsPlanItem => this.getApsPlanIdentifier(apsPlanItem)!);
      const apsPlansToAdd = apsPlans.filter(apsPlanItem => {
        const apsPlanIdentifier = this.getApsPlanIdentifier(apsPlanItem);
        if (apsPlanCollectionIdentifiers.includes(apsPlanIdentifier)) {
          return false;
        }
        apsPlanCollectionIdentifiers.push(apsPlanIdentifier);
        return true;
      });
      return [...apsPlansToAdd, ...apsPlanCollection];
    }
    return apsPlanCollection;
  }

  protected convertDateFromClient<T extends IApsPlan | NewApsPlan | PartialUpdateApsPlan>(apsPlan: T): RestOf<T> {
    return {
      ...apsPlan,
      starts: apsPlan.starts?.format(DATE_FORMAT) ?? null,
      ends: apsPlan.ends?.format(DATE_FORMAT) ?? null,
      created: apsPlan.created?.toJSON() ?? null,
      updated: apsPlan.updated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restApsPlan: RestApsPlan): IApsPlan {
    return {
      ...restApsPlan,
      starts: restApsPlan.starts ? dayjs(restApsPlan.starts) : undefined,
      ends: restApsPlan.ends ? dayjs(restApsPlan.ends) : undefined,
      created: restApsPlan.created ? dayjs(restApsPlan.created) : undefined,
      updated: restApsPlan.updated ? dayjs(restApsPlan.updated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestApsPlan>): HttpResponse<IApsPlan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestApsPlan[]>): HttpResponse<IApsPlan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
