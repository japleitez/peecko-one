import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsUser } from '../aps-user.model';
import { ApsUserService } from '../service/aps-user.service';

export const apsUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsUserService)
      .find(id)
      .pipe(
        mergeMap((apsUser: HttpResponse<IApsUser>) => {
          if (apsUser.body) {
            return of(apsUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsUserResolve;
