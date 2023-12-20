import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsOrder } from '../aps-order.model';
import { ApsOrderService } from '../service/aps-order.service';

export const apsOrderResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsOrder> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsOrderService)
      .find(id)
      .pipe(
        mergeMap((apsOrder: HttpResponse<IApsOrder>) => {
          if (apsOrder.body) {
            return of(apsOrder.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsOrderResolve;
