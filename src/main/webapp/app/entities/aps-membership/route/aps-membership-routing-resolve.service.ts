import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsMembership } from '../aps-membership.model';
import { ApsMembershipService } from '../service/aps-membership.service';

export const apsMembershipResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsMembership> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsMembershipService)
      .find(id)
      .pipe(
        mergeMap((apsMembership: HttpResponse<IApsMembership>) => {
          if (apsMembership.body) {
            return of(apsMembership.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsMembershipResolve;
