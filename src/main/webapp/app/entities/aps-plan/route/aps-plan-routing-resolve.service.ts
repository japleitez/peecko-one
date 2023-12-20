import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsPlan } from '../aps-plan.model';
import { ApsPlanService } from '../service/aps-plan.service';

export const apsPlanResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsPlan> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsPlanService)
      .find(id)
      .pipe(
        mergeMap((apsPlan: HttpResponse<IApsPlan>) => {
          if (apsPlan.body) {
            return of(apsPlan.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsPlanResolve;
