import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApsDevice } from '../aps-device.model';
import { ApsDeviceService } from '../service/aps-device.service';

export const apsDeviceResolve = (route: ActivatedRouteSnapshot): Observable<null | IApsDevice> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApsDeviceService)
      .find(id)
      .pipe(
        mergeMap((apsDevice: HttpResponse<IApsDevice>) => {
          if (apsDevice.body) {
            return of(apsDevice.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default apsDeviceResolve;
