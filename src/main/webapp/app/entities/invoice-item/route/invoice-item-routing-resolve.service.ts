import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceItem } from '../invoice-item.model';
import { InvoiceItemService } from '../service/invoice-item.service';

export const invoiceItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IInvoiceItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(InvoiceItemService)
      .find(id)
      .pipe(
        mergeMap((invoiceItem: HttpResponse<IInvoiceItem>) => {
          if (invoiceItem.body) {
            return of(invoiceItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default invoiceItemResolve;
