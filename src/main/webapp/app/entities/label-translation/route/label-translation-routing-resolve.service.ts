import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILabelTranslation } from '../label-translation.model';
import { LabelTranslationService } from '../service/label-translation.service';

export const labelTranslationResolve = (route: ActivatedRouteSnapshot): Observable<null | ILabelTranslation> => {
  const id = route.params['id'];
  if (id) {
    return inject(LabelTranslationService)
      .find(id)
      .pipe(
        mergeMap((labelTranslation: HttpResponse<ILabelTranslation>) => {
          if (labelTranslation.body) {
            return of(labelTranslation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default labelTranslationResolve;
