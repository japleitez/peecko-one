import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsUserComponent } from './list/aps-user.component';
import { ApsUserDetailComponent } from './detail/aps-user-detail.component';
import { ApsUserUpdateComponent } from './update/aps-user-update.component';
import ApsUserResolve from './route/aps-user-routing-resolve.service';

const apsUserRoute: Routes = [
  {
    path: '',
    component: ApsUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsUserDetailComponent,
    resolve: {
      apsUser: ApsUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsUserUpdateComponent,
    resolve: {
      apsUser: ApsUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsUserUpdateComponent,
    resolve: {
      apsUser: ApsUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsUserRoute;
