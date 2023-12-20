import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApsDevice, NewApsDevice } from '../aps-device.model';

export type PartialUpdateApsDevice = Partial<IApsDevice> & Pick<IApsDevice, 'id'>;

type RestOf<T extends IApsDevice | NewApsDevice> = Omit<T, 'installedOn'> & {
  installedOn?: string | null;
};

export type RestApsDevice = RestOf<IApsDevice>;

export type NewRestApsDevice = RestOf<NewApsDevice>;

export type PartialUpdateRestApsDevice = RestOf<PartialUpdateApsDevice>;

export type EntityResponseType = HttpResponse<IApsDevice>;
export type EntityArrayResponseType = HttpResponse<IApsDevice[]>;

@Injectable({ providedIn: 'root' })
export class ApsDeviceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aps-devices');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(apsDevice: NewApsDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsDevice);
    return this.http
      .post<RestApsDevice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(apsDevice: IApsDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsDevice);
    return this.http
      .put<RestApsDevice>(`${this.resourceUrl}/${this.getApsDeviceIdentifier(apsDevice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(apsDevice: PartialUpdateApsDevice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apsDevice);
    return this.http
      .patch<RestApsDevice>(`${this.resourceUrl}/${this.getApsDeviceIdentifier(apsDevice)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestApsDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestApsDevice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApsDeviceIdentifier(apsDevice: Pick<IApsDevice, 'id'>): number {
    return apsDevice.id;
  }

  compareApsDevice(o1: Pick<IApsDevice, 'id'> | null, o2: Pick<IApsDevice, 'id'> | null): boolean {
    return o1 && o2 ? this.getApsDeviceIdentifier(o1) === this.getApsDeviceIdentifier(o2) : o1 === o2;
  }

  addApsDeviceToCollectionIfMissing<Type extends Pick<IApsDevice, 'id'>>(
    apsDeviceCollection: Type[],
    ...apsDevicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const apsDevices: Type[] = apsDevicesToCheck.filter(isPresent);
    if (apsDevices.length > 0) {
      const apsDeviceCollectionIdentifiers = apsDeviceCollection.map(apsDeviceItem => this.getApsDeviceIdentifier(apsDeviceItem)!);
      const apsDevicesToAdd = apsDevices.filter(apsDeviceItem => {
        const apsDeviceIdentifier = this.getApsDeviceIdentifier(apsDeviceItem);
        if (apsDeviceCollectionIdentifiers.includes(apsDeviceIdentifier)) {
          return false;
        }
        apsDeviceCollectionIdentifiers.push(apsDeviceIdentifier);
        return true;
      });
      return [...apsDevicesToAdd, ...apsDeviceCollection];
    }
    return apsDeviceCollection;
  }

  protected convertDateFromClient<T extends IApsDevice | NewApsDevice | PartialUpdateApsDevice>(apsDevice: T): RestOf<T> {
    return {
      ...apsDevice,
      installedOn: apsDevice.installedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restApsDevice: RestApsDevice): IApsDevice {
    return {
      ...restApsDevice,
      installedOn: restApsDevice.installedOn ? dayjs(restApsDevice.installedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestApsDevice>): HttpResponse<IApsDevice> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestApsDevice[]>): HttpResponse<IApsDevice[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
