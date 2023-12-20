import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArticleCategory } from '../article-category.model';
import { ArticleCategoryService } from '../service/article-category.service';

export const articleCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IArticleCategory> => {
  const id = route.params['id'];
  if (id) {
    return inject(ArticleCategoryService)
      .find(id)
      .pipe(
        mergeMap((articleCategory: HttpResponse<IArticleCategory>) => {
          if (articleCategory.body) {
            return of(articleCategory.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default articleCategoryResolve;
