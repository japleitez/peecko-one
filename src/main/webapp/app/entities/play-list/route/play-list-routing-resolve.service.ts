import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayList } from '../play-list.model';
import { PlayListService } from '../service/play-list.service';

export const playListResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlayList> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlayListService)
      .find(id)
      .pipe(
        mergeMap((playList: HttpResponse<IPlayList>) => {
          if (playList.body) {
            return of(playList.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default playListResolve;
