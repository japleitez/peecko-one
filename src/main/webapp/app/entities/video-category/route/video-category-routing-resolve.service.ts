import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVideoCategory } from '../video-category.model';
import { VideoCategoryService } from '../service/video-category.service';

export const videoCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IVideoCategory> => {
  const id = route.params['id'];
  if (id) {
    return inject(VideoCategoryService)
      .find(id)
      .pipe(
        mergeMap((videoCategory: HttpResponse<IVideoCategory>) => {
          if (videoCategory.body) {
            return of(videoCategory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default videoCategoryResolve;
