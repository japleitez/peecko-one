import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsPricing, NewApsPricing } from '../aps-pricing.model';

export type PartialUpdateApsPricing = Partial<IApsPricing> & Pick<IApsPricing, 'id'>;

export type EntityResponseType = HttpResponse<IApsPricing>;
export type EntityArrayResponseType = HttpResponse<IApsPricing[]>;

@Injectable({ providedIn: 'root' })
export class ApsPricingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-pricings');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsPricing: NewApsPricing): Observable<EntityResponseType> {
    return this.http.post<IApsPricing>(this.resourceUrl, apsPricing, { observe: 'response' });
  }

  update(apsPricing: IApsPricing): Observable<EntityResponseType> {
    return this.http.put<IApsPricing>(`${this.resourceUrl}/${this.getApsPricingIdentifier(apsPricing)}`, apsPricing, {
      observe: 'response',
    });
  }

  partialUpdate(apsPricing: PartialUpdateApsPricing): Observable<EntityResponseType> {
    return this.http.patch<IApsPricing>(`${this.resourceUrl}/${this.getApsPricingIdentifier(apsPricing)}`, apsPricing, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApsPricing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApsPricing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsPricingIdentifier(apsPricing: Pick<IApsPricing, 'id'>): number {
    return apsPricing.id;
  }

  compareApsPricing(o1: Pick<IApsPricing, 'id'> | null, o2: Pick<IApsPricing, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsPricingIdentifier(o1) === this.getApsPricingIdentifier(o2) : o1 === o2;
  }

  addApsPricingToCollectionIfMissing<Type extends Pick<IApsPricing, 'id'>>(
    apsPricingCollection: Type[],
    ...apsPricingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsPricings: Type[] = apsPricingsToCheck.filter(isPresent);
    if (apsPricings.length > 0) {
      const apsPricingCollectionIdentifiers = apsPricingCollection.map(apsPricingItem => this.getApsPricingIdentifier(apsPricingItem)!);
      const apsPricingsToAdd = apsPricings.filter(apsPricingItem => {
        const apsPricingIdentifier = this.getApsPricingIdentifier(apsPricingItem);
        if (apsPricingCollectionIdentifiers.includes(apsPricingIdentifier)) {
          return false;
        }
        apsPricingCollectionIdentifiers.push(apsPricingIdentifier);
        return true;
      });
      return [...apsPricingsToAdd, ...apsPricingCollection];
    }
    return apsPricingCollection;
  }
}
