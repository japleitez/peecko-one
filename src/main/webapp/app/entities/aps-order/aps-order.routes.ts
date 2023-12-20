import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsOrderComponent } from './list/aps-order.component';
import { ApsOrderDetailComponent } from './detail/aps-order-detail.component';
import { ApsOrderUpdateComponent } from './update/aps-order-update.component';
import ApsOrderResolve from './route/aps-order-routing-resolve.service';

const apsOrderRoute: Routes = [
  {
    path: '',
    component: ApsOrderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsOrderDetailComponent,
    resolve: {
      apsOrder: ApsOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsOrderUpdateComponent,
    resolve: {
      apsOrder: ApsOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsOrderUpdateComponent,
    resolve: {
      apsOrder: ApsOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsOrderRoute;
