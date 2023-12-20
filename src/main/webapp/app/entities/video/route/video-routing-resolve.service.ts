import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVideo } from '../video.model';
import { VideoService } from '../service/video.service';

export const videoResolve = (route: ActivatedRouteSnapshot): Observable<null | IVideo> => {
  const id = route.params['id'];
  if (id) {
    return inject(VideoService)
      .find(id)
      .pipe(
        mergeMap((video: HttpResponse<IVideo>) => {
          if (video.body) {
            return of(video.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default videoResolve;
