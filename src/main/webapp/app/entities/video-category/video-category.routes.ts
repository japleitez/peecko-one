import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VideoCategoryComponent } from './list/video-category.component';
import { VideoCategoryDetailComponent } from './detail/video-category-detail.component';
import { VideoCategoryUpdateComponent } from './update/video-category-update.component';
import VideoCategoryResolve from './route/video-category-routing-resolve.service';

const videoCategoryRoute: Routes = [
  {
    path: '',
    component: VideoCategoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VideoCategoryDetailComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VideoCategoryUpdateComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VideoCategoryUpdateComponent,
    resolve: {
      videoCategory: VideoCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default videoCategoryRoute;
