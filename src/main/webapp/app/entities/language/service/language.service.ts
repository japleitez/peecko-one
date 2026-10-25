import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILanguage, NewLanguage } from '../language.model';

export type PartialUpdateLanguage = Partial<ILanguage> & Pick<ILanguage, 'id'>;

export type EntityResponseType = HttpResponse<ILanguage>;
export type EntityArrayResponseType = HttpResponse<ILanguage[]>;

@Injectable({ providedIn: 'root' })
export class LanguageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/languages');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(language: NewLanguage): Observable<EntityResponseType> {
    return this.http.post<ILanguage>(this.resourceUrl, language, { observe: 'response' });
  }

  update(language: ILanguage): Observable<EntityResponseType> {
    return this.http.put<ILanguage>(`${this.resourceUrl}/${this.getLanguageIdentifier(language)}`, language, { observe: 'response' });
  }

  partialUpdate(language: PartialUpdateLanguage): Observable<EntityResponseType> {
    return this.http.patch<ILanguage>(`${this.resourceUrl}/${this.getLanguageIdentifier(language)}`, language, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILanguage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILanguage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  /**
   * Loads the codes of the active languages (EN, FR, ...), ordered by language id.
   * When the languages cannot be loaded, the failure is logged (the HTTP error is also reported by the error alert)
   * and an empty list is returned, so the callers always get a usable list.
   */
  queryActiveCodes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.resourceUrl}/codes`).pipe(
      catchError((error: unknown) => {
        console.error('Failed to load the language codes', error);
        return of([]);
      }),
    );
  }

  /**
   * Adds the current value of a record to the language codes if it is missing (e.g. an inactive language),
   * so that editing the record does not lose its language.
   */
  addLanguageCodeIfMissing(codes: string[], current?: string | null): string[] {
    if (current && !codes.includes(current)) {
      return [...codes, current];
    }
    return codes;
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLanguageIdentifier(language: Pick<ILanguage, 'id'>): number {
    return language.id;
  }

  compareLanguage(o1: Pick<ILanguage, 'id'> | null, o2: Pick<ILanguage, 'id'> | null): boolean {
    return o1 && o2 ? this.getLanguageIdentifier(o1) === this.getLanguageIdentifier(o2) : o1 === o2;
  }

  addLanguageToCollectionIfMissing<Type extends Pick<ILanguage, 'id'>>(
    languageCollection: Type[],
    ...languagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const languages: Type[] = languagesToCheck.filter(isPresent);
    if (languages.length > 0) {
      const languageCollectionIdentifiers = languageCollection.map(languageItem => this.getLanguageIdentifier(languageItem)!);
      const languagesToAdd = languages.filter(languageItem => {
        const languageIdentifier = this.getLanguageIdentifier(languageItem);
        if (languageCollectionIdentifiers.includes(languageIdentifier)) {
          return false;
        }
        languageCollectionIdentifiers.push(languageIdentifier);
        return true;
      });
      return [...languagesToAdd, ...languageCollection];
    }
    return languageCollection;
  }
}
