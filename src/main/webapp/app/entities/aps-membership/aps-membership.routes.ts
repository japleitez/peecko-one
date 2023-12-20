import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsMembershipComponent } from './list/aps-membership.component';
import { ApsMembershipDetailComponent } from './detail/aps-membership-detail.component';
import { ApsMembershipUpdateComponent } from './update/aps-membership-update.component';
import ApsMembershipResolve from './route/aps-membership-routing-resolve.service';

const apsMembershipRoute: Routes = [
  {
    path: '',
    component: ApsMembershipComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsMembershipDetailComponent,
    resolve: {
      apsMembership: ApsMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsMembershipUpdateComponent,
    resolve: {
      apsMembership: ApsMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsMembershipUpdateComponent,
    resolve: {
      apsMembership: ApsMembershipResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsMembershipRoute;
