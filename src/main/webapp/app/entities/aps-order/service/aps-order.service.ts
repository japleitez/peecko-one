import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsOrder, NewApsOrder } from '../aps-order.model';

export type PartialUpdateApsOrder = Partial<IApsOrder> & Pick<IApsOrder, 'id'>;

export type EntityResponseType = HttpResponse<IApsOrder>;
export type EntityArrayResponseType = HttpResponse<IApsOrder[]>;

@Injectable({ providedIn: 'root' })
export class ApsOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-orders');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsOrder: NewApsOrder): Observable<EntityResponseType> {
    return this.http.post<IApsOrder>(this.resourceUrl, apsOrder, { observe: 'response' });
  }

  update(apsOrder: IApsOrder): Observable<EntityResponseType> {
    return this.http.put<IApsOrder>(`${this.resourceUrl}/${this.getApsOrderIdentifier(apsOrder)}`, apsOrder, { observe: 'response' });
  }

  partialUpdate(apsOrder: PartialUpdateApsOrder): Observable<EntityResponseType> {
    return this.http.patch<IApsOrder>(`${this.resourceUrl}/${this.getApsOrderIdentifier(apsOrder)}`, apsOrder, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApsOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApsOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  batchGenerate(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApsOrder[]>(`${this.resourceUrl}/batch/generate`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsOrderIdentifier(apsOrder: Pick<IApsOrder, 'id'>): number {
    return apsOrder.id;
  }

  compareApsOrder(o1: Pick<IApsOrder, 'id'> | null, o2: Pick<IApsOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsOrderIdentifier(o1) === this.getApsOrderIdentifier(o2) : o1 === o2;
  }

  addApsOrderToCollectionIfMissing<Type extends Pick<IApsOrder, 'id'>>(
    apsOrderCollection: Type[],
    ...apsOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsOrders: Type[] = apsOrdersToCheck.filter(isPresent);
    if (apsOrders.length > 0) {
      const apsOrderCollectionIdentifiers = apsOrderCollection.map(apsOrderItem => this.getApsOrderIdentifier(apsOrderItem)!);
      const apsOrdersToAdd = apsOrders.filter(apsOrderItem => {
        const apsOrderIdentifier = this.getApsOrderIdentifier(apsOrderItem);
        if (apsOrderCollectionIdentifiers.includes(apsOrderIdentifier)) {
          return false;
        }
        apsOrderCollectionIdentifiers.push(apsOrderIdentifier);
        return true;
      });
      return [...apsOrdersToAdd, ...apsOrderCollection];
    }
    return apsOrderCollection;
  }
}
