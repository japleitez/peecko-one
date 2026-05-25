import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrialLicense } from '../trial-license.model';

export type PartialUpdateTrialLicense = Partial<ITrialLicense> & Pick<ITrialLicense, 'license'>;

type RestOf<T extends Pick<ITrialLicense, 'license'>> = Omit<T, 'expirationDate'> & {
  expirationDate?: string | null;
};

export type RestTrialLicense = RestOf<ITrialLicense>;
export type PartialUpdateRestTrialLicense = RestOf<PartialUpdateTrialLicense>;

export type TrialLicenseResponseType = HttpResponse<ITrialLicense>;
export type TrialLicenseArrayResponseType = HttpResponse<ITrialLicense[]>;

@Injectable({ providedIn: 'root' })
export class TrialLicenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trial-licenses');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(trialLicense: ITrialLicense): Observable<TrialLicenseResponseType> {
    const copy = this.convertDateFromClient(trialLicense);
    return this.http
      .post<RestTrialLicense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trialLicense: ITrialLicense): Observable<TrialLicenseResponseType> {
    const copy = this.convertDateFromClient(trialLicense);
    return this.http
      .put<RestTrialLicense>(`${this.resourceUrl}/${this.getTrialLicenseIdentifier(trialLicense)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trialLicense: PartialUpdateTrialLicense): Observable<TrialLicenseResponseType> {
    const copy = this.convertDateFromClient(trialLicense);
    return this.http
      .patch<RestTrialLicense>(`${this.resourceUrl}/${this.getTrialLicenseIdentifier(trialLicense)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(license: string): Observable<TrialLicenseResponseType> {
    return this.http
      .get<RestTrialLicense>(`${this.resourceUrl}/${license}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<TrialLicenseArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrialLicense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(license: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${license}`, { observe: 'response' });
  }

  getTrialLicenseIdentifier(trialLicense: Pick<ITrialLicense, 'license'>): string {
    return trialLicense.license;
  }

  compareTrialLicense(o1: Pick<ITrialLicense, 'license'> | null, o2: Pick<ITrialLicense, 'license'> | null): boolean {
    return o1 && o2 ? this.getTrialLicenseIdentifier(o1) === this.getTrialLicenseIdentifier(o2) : o1 === o2;
  }

  addTrialLicenseToCollectionIfMissing<Type extends Pick<ITrialLicense, 'license'>>(
    trialLicenseCollection: Type[],
    ...trialLicensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trialLicenses: Type[] = trialLicensesToCheck.filter(isPresent);
    if (trialLicenses.length > 0) {
      const trialLicenseCollectionIdentifiers = trialLicenseCollection.map(item => this.getTrialLicenseIdentifier(item)!);
      const trialLicensesToAdd = trialLicenses.filter(item => {
        const identifier = this.getTrialLicenseIdentifier(item);
        if (trialLicenseCollectionIdentifiers.includes(identifier)) {
          return false;
        }
        trialLicenseCollectionIdentifiers.push(identifier);
        return true;
      });
      return [...trialLicensesToAdd, ...trialLicenseCollection];
    }
    return trialLicenseCollection;
  }

  protected convertDateFromClient<T extends Pick<ITrialLicense, 'license'> & { expirationDate?: dayjs.Dayjs | null }>(
    trialLicense: T,
  ): RestOf<T> {
    return {
      ...trialLicense,
      expirationDate: trialLicense.expirationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTrialLicense: RestTrialLicense): ITrialLicense {
    return {
      ...restTrialLicense,
      expirationDate: restTrialLicense.expirationDate ? dayjs(restTrialLicense.expirationDate) : null,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrialLicense>): HttpResponse<ITrialLicense> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrialLicense[]>): HttpResponse<ITrialLicense[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
