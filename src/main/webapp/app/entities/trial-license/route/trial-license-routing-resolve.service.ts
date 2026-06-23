import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrialLicense } from '../trial-license.model';
import { TrialLicenseService } from '../service/trial-license.service';

export const trialLicenseResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrialLicense> => {
  const id = route.params['id'];
  if (id) {
    return inject(TrialLicenseService)
      .find(id)
      .pipe(
        mergeMap((trialLicense: HttpResponse<ITrialLicense>) => {
          if (trialLicense.body) {
            return of(trialLicense.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default trialLicenseResolve;
