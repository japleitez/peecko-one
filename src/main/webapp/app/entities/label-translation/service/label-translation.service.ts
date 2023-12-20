import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILabelTranslation, NewLabelTranslation } from '../label-translation.model';

export type PartialUpdateLabelTranslation = Partial<ILabelTranslation> & Pick<ILabelTranslation, 'id'>;

export type EntityResponseType = HttpResponse<ILabelTranslation>;
export type EntityArrayResponseType = HttpResponse<ILabelTranslation[]>;

@Injectable({ providedIn: 'root' })
export class LabelTranslationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/label-translations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(labelTranslation: NewLabelTranslation): Observable<EntityResponseType> {
    return this.http.post<ILabelTranslation>(this.resourceUrl, labelTranslation, { observe: 'response' });
  }

  update(labelTranslation: ILabelTranslation): Observable<EntityResponseType> {
    return this.http.put<ILabelTranslation>(
      `${this.resourceUrl}/${this.getLabelTranslationIdentifier(labelTranslation)}`,
      labelTranslation,
      { observe: 'response' },
    );
  }

  partialUpdate(labelTranslation: PartialUpdateLabelTranslation): Observable<EntityResponseType> {
    return this.http.patch<ILabelTranslation>(
      `${this.resourceUrl}/${this.getLabelTranslationIdentifier(labelTranslation)}`,
      labelTranslation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILabelTranslation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILabelTranslation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLabelTranslationIdentifier(labelTranslation: Pick<ILabelTranslation, 'id'>): number {
    return labelTranslation.id;
  }

  compareLabelTranslation(o1: Pick<ILabelTranslation, 'id'> | null, o2: Pick<ILabelTranslation, 'id'> | null): boolean {
    return o1 && o2 ? this.getLabelTranslationIdentifier(o1) === this.getLabelTranslationIdentifier(o2) : o1 === o2;
  }

  addLabelTranslationToCollectionIfMissing<Type extends Pick<ILabelTranslation, 'id'>>(
    labelTranslationCollection: Type[],
    ...labelTranslationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const labelTranslations: Type[] = labelTranslationsToCheck.filter(isPresent);
    if (labelTranslations.length > 0) {
      const labelTranslationCollectionIdentifiers = labelTranslationCollection.map(
        labelTranslationItem => this.getLabelTranslationIdentifier(labelTranslationItem)!,
      );
      const labelTranslationsToAdd = labelTranslations.filter(labelTranslationItem => {
        const labelTranslationIdentifier = this.getLabelTranslationIdentifier(labelTranslationItem);
        if (labelTranslationCollectionIdentifiers.includes(labelTranslationIdentifier)) {
          return false;
        }
        labelTranslationCollectionIdentifiers.push(labelTranslationIdentifier);
        return true;
      });
      return [...labelTranslationsToAdd, ...labelTranslationCollection];
    }
    return labelTranslationCollection;
  }
}
