import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsPricingComponent } from './list/aps-pricing.component';
import { ApsPricingDetailComponent } from './detail/aps-pricing-detail.component';
import { ApsPricingUpdateComponent } from './update/aps-pricing-update.component';
import ApsPricingResolve from './route/aps-pricing-routing-resolve.service';

const apsPricingRoute: Routes = [
  {
    path: '',
    component: ApsPricingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsPricingDetailComponent,
    resolve: {
      apsPricing: ApsPricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsPricingUpdateComponent,
    resolve: {
      apsPricing: ApsPricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsPricingUpdateComponent,
    resolve: {
      apsPricing: ApsPricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsPricingRoute;
