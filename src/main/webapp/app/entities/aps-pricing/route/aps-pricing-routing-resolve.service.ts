import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsPricing } from '../aps-pricing.model';
import { ApsPricingService } from '../service/aps-pricing.service';

export const apsPricingResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsPricing> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsPricingService)
      .find(id)
      .pipe(
        mergeMap((apsPricing: HttpResponse<IApsPricing>) => {
          if (apsPricing.body) {
            return of(apsPricing.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsPricingResolve;
