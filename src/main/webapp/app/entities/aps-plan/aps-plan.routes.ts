import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsPlanComponent } from './list/aps-plan.component';
import { ApsPlanDetailComponent } from './detail/aps-plan-detail.component';
import { ApsPlanUpdateComponent } from './update/aps-plan-update.component';
import ApsPlanResolve from './route/aps-plan-routing-resolve.service';

const apsPlanRoute: Routes = [
  {
    path: '',
    component: ApsPlanComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsPlanDetailComponent,
    resolve: {
      apsPlan: ApsPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsPlanUpdateComponent,
    resolve: {
      apsPlan: ApsPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsPlanUpdateComponent,
    resolve: {
      apsPlan: ApsPlanResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsPlanRoute;
