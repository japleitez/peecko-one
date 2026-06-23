import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountry } from '../country.model';

export type PartialUpdateCountry = Partial<ICountry> & Pick<ICountry, 'code'>;

export type CountryResponseType = HttpResponse<ICountry>;
export type CountryArrayResponseType = HttpResponse<ICountry[]>;

@Injectable({ providedIn: 'root' })
export class CountryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/countries');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(country: ICountry): Observable<CountryResponseType> {
    return this.http.post<ICountry>(this.resourceUrl, country, { observe: 'response' });
  }

  update(country: ICountry): Observable<CountryResponseType> {
    return this.http.put<ICountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, country, { observe: 'response' });
  }

  partialUpdate(country: PartialUpdateCountry): Observable<CountryResponseType> {
    return this.http.patch<ICountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, country, { observe: 'response' });
  }

  find(code: string): Observable<CountryResponseType> {
    return this.http.get<ICountry>(`${this.resourceUrl}/${code}`, { observe: 'response' });
  }

  query(req?: any): Observable<CountryArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(code: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${code}`, { observe: 'response' });
  }

  getCountryIdentifier(country: Pick<ICountry, 'code'>): string {
    return country.code;
  }

  compareCountry(o1: Pick<ICountry, 'code'> | null, o2: Pick<ICountry, 'code'> | null): boolean {
    return o1 && o2 ? this.getCountryIdentifier(o1) === this.getCountryIdentifier(o2) : o1 === o2;
  }

  addCountryToCollectionIfMissing<Type extends Pick<ICountry, 'code'>>(
    countryCollection: Type[],
    ...countriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const countries: Type[] = countriesToCheck.filter(isPresent);
    if (countries.length > 0) {
      const countryCollectionIdentifiers = countryCollection.map(countryItem => this.getCountryIdentifier(countryItem)!);
      const countriesToAdd = countries.filter(countryItem => {
        const countryIdentifier = this.getCountryIdentifier(countryItem);
        if (countryCollectionIdentifiers.includes(countryIdentifier)) {
          return false;
        }
        countryCollectionIdentifiers.push(countryIdentifier);
        return true;
      });
      return [...countriesToAdd, ...countryCollection];
    }
    return countryCollection;
  }
}
