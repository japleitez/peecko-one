import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVideoItem } from '../video-item.model';
import { VideoItemService } from '../service/video-item.service';

export const videoItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IVideoItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(VideoItemService)
      .find(id)
      .pipe(
        mergeMap((videoItem: HttpResponse<IVideoItem>) => {
          if (videoItem.body) {
            return of(videoItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default videoItemResolve;
