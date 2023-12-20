import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CoachComponent } from './list/coach.component';
import { CoachDetailComponent } from './detail/coach-detail.component';
import { CoachUpdateComponent } from './update/coach-update.component';
import CoachResolve from './route/coach-routing-resolve.service';

const coachRoute: Routes = [
  {
    path: '',
    component: CoachComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoachDetailComponent,
    resolve: {
      coach: CoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoachUpdateComponent,
    resolve: {
      coach: CoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoachUpdateComponent,
    resolve: {
      coach: CoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default coachRoute;
