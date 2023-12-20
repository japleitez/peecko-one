import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArticleSeries } from '../article-series.model';
import { ArticleSeriesService } from '../service/article-series.service';

export const articleSeriesResolve = (route: ActivatedRouteSnapshot): Observable<null | IArticleSeries> => {
  const id = route.params['id'];
  if (id) {
    return inject(ArticleSeriesService)
      .find(id)
      .pipe(
        mergeMap((articleSeries: HttpResponse<IArticleSeries>) => {
          if (articleSeries.body) {
            return of(articleSeries.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default articleSeriesResolve;
