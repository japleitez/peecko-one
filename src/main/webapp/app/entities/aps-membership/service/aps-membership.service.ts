import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsMembership, NewApsMembership } from '../aps-membership.model';

export type PartialUpdateApsMembership = Partial<IApsMembership> & Pick<IApsMembership, 'id'>;

export type EntityResponseType = HttpResponse<IApsMembership>;
export type EntityArrayResponseType = HttpResponse<IApsMembership[]>;

@Injectable({ providedIn: 'root' })
export class ApsMembershipService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-memberships');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsMembership: NewApsMembership): Observable<EntityResponseType> {
    return this.http.post<IApsMembership>(this.resourceUrl, apsMembership, { observe: 'response' });
  }

  update(apsMembership: IApsMembership): Observable<EntityResponseType> {
    return this.http.put<IApsMembership>(`${this.resourceUrl}/${this.getApsMembershipIdentifier(apsMembership)}`, apsMembership, {
      observe: 'response',
    });
  }

  partialUpdate(apsMembership: PartialUpdateApsMembership): Observable<EntityResponseType> {
    return this.http.patch<IApsMembership>(`${this.resourceUrl}/${this.getApsMembershipIdentifier(apsMembership)}`, apsMembership, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApsMembership>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApsMembership[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsMembershipIdentifier(apsMembership: Pick<IApsMembership, 'id'>): number {
    return apsMembership.id;
  }

  compareApsMembership(o1: Pick<IApsMembership, 'id'> | null, o2: Pick<IApsMembership, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsMembershipIdentifier(o1) === this.getApsMembershipIdentifier(o2) : o1 === o2;
  }

  addApsMembershipToCollectionIfMissing<Type extends Pick<IApsMembership, 'id'>>(
    apsMembershipCollection: Type[],
    ...apsMembershipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsMemberships: Type[] = apsMembershipsToCheck.filter(isPresent);
    if (apsMemberships.length > 0) {
      const apsMembershipCollectionIdentifiers = apsMembershipCollection.map(
        apsMembershipItem => this.getApsMembershipIdentifier(apsMembershipItem)!,
      );
      const apsMembershipsToAdd = apsMemberships.filter(apsMembershipItem => {
        const apsMembershipIdentifier = this.getApsMembershipIdentifier(apsMembershipItem);
        if (apsMembershipCollectionIdentifiers.includes(apsMembershipIdentifier)) {
          return false;
        }
        apsMembershipCollectionIdentifiers.push(apsMembershipIdentifier);
        return true;
      });
      return [...apsMembershipsToAdd, ...apsMembershipCollection];
    }
    return apsMembershipCollection;
  }
}
