import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVideoItem, NewVideoItem } from '../video-item.model';

export type PartialUpdateVideoItem = Partial<IVideoItem> & Pick<IVideoItem, 'id'>;

export type EntityResponseType = HttpResponse<IVideoItem>;
export type EntityArrayResponseType = HttpResponse<IVideoItem[]>;

@Injectable({ providedIn: 'root' })
export class VideoItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/video-items');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(videoItem: NewVideoItem): Observable<EntityResponseType> {
    return this.http.post<IVideoItem>(this.resourceUrl, videoItem, { observe: 'response' });
  }

  update(videoItem: IVideoItem): Observable<EntityResponseType> {
    return this.http.put<IVideoItem>(`${this.resourceUrl}/${this.getVideoItemIdentifier(videoItem)}`, videoItem, { observe: 'response' });
  }

  partialUpdate(videoItem: PartialUpdateVideoItem): Observable<EntityResponseType> {
    return this.http.patch<IVideoItem>(`${this.resourceUrl}/${this.getVideoItemIdentifier(videoItem)}`, videoItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVideoItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVideoItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVideoItemIdentifier(videoItem: Pick<IVideoItem, 'id'>): number {
    return videoItem.id;
  }

  compareVideoItem(o1: Pick<IVideoItem, 'id'> | null, o2: Pick<IVideoItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getVideoItemIdentifier(o1) === this.getVideoItemIdentifier(o2) : o1 === o2;
  }

  addVideoItemToCollectionIfMissing<Type extends Pick<IVideoItem, 'id'>>(
    videoItemCollection: Type[],
    ...videoItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const videoItems: Type[] = videoItemsToCheck.filter(isPresent);
    if (videoItems.length > 0) {
      const videoItemCollectionIdentifiers = videoItemCollection.map(videoItemItem => this.getVideoItemIdentifier(videoItemItem)!);
      const videoItemsToAdd = videoItems.filter(videoItemItem => {
        const videoItemIdentifier = this.getVideoItemIdentifier(videoItemItem);
        if (videoItemCollectionIdentifiers.includes(videoItemIdentifier)) {
          return false;
        }
        videoItemCollectionIdentifiers.push(videoItemIdentifier);
        return true;
      });
      return [...videoItemsToAdd, ...videoItemCollection];
    }
    return videoItemCollection;
  }
}
