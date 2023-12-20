import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InvoiceItemComponent } from './list/invoice-item.component';
import { InvoiceItemDetailComponent } from './detail/invoice-item-detail.component';
import { InvoiceItemUpdateComponent } from './update/invoice-item-update.component';
import InvoiceItemResolve from './route/invoice-item-routing-resolve.service';

const invoiceItemRoute: Routes = [
  {
    path: '',
    component: InvoiceItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceItemDetailComponent,
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceItemUpdateComponent,
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceItemUpdateComponent,
    resolve: {
      invoiceItem: InvoiceItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default invoiceItemRoute;
