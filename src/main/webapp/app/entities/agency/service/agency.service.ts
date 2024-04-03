import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgency, NewAgency } from '../agency.model';

export type PartialUpdateAgency = Partial<IAgency> & Pick<IAgency, 'id'>;

type RestOf<T extends IAgency | NewAgency> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

export type RestAgency = RestOf<IAgency>;

export type NewRestAgency = RestOf<NewAgency>;

export type PartialUpdateRestAgency = RestOf<PartialUpdateAgency>;

export type AgencyResponseType = HttpResponse<IAgency>;
export type AgencyArrayResponseType = HttpResponse<IAgency[]>;

@Injectable({ providedIn: 'root' })
export class AgencyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agencies');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(agency: NewAgency): Observable<AgencyResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .post<RestAgency>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(agency: IAgency): Observable<AgencyResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .put<RestAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(agency: PartialUpdateAgency): Observable<AgencyResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .patch<RestAgency>(`${this.resourceUrl}/${this.getAgencyIdentifier(agency)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<AgencyResponseType> {
    return this.http
      .get<RestAgency>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<AgencyArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAgency[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgencyIdentifier(agency: Pick<IAgency, 'id'>): number {
    return agency.id;
  }

  compareAgency(o1: Pick<IAgency, 'id'> | null, o2: Pick<IAgency, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgencyIdentifier(o1) === this.getAgencyIdentifier(o2) : o1 === o2;
  }

  addAgencyToCollectionIfMissing<Type extends Pick<IAgency, 'id'>>(
    agencyCollection: Type[],
    ...agenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agencies: Type[] = agenciesToCheck.filter(isPresent);
    if (agencies.length > 0) {
      const agencyCollectionIdentifiers = agencyCollection.map(agencyItem => this.getAgencyIdentifier(agencyItem)!);
      const agenciesToAdd = agencies.filter(agencyItem => {
        const agencyIdentifier = this.getAgencyIdentifier(agencyItem);
        if (agencyCollectionIdentifiers.includes(agencyIdentifier)) {
          return false;
        }
        agencyCollectionIdentifiers.push(agencyIdentifier);
        return true;
      });
      return [...agenciesToAdd, ...agencyCollection];
    }
    return agencyCollection;
  }

  protected convertDateFromClient<T extends IAgency | NewAgency | PartialUpdateAgency>(agency: T): RestOf<T> {
    return {
      ...agency,
      created: agency.created?.toJSON() ?? null,
      updated: agency.updated?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAgency: RestAgency): IAgency {
    return {
      ...restAgency,
      created: restAgency.created ? dayjs(restAgency.created) : undefined,
      updated: restAgency.updated ? dayjs(restAgency.updated) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAgency>): HttpResponse<IAgency> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAgency[]>): HttpResponse<IAgency[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
