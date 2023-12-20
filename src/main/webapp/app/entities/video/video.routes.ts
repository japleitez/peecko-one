import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VideoComponent } from './list/video.component';
import { VideoDetailComponent } from './detail/video-detail.component';
import { VideoUpdateComponent } from './update/video-update.component';
import VideoResolve from './route/video-routing-resolve.service';

const videoRoute: Routes = [
  {
    path: '',
    component: VideoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VideoDetailComponent,
    resolve: {
      video: VideoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VideoUpdateComponent,
    resolve: {
      video: VideoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VideoUpdateComponent,
    resolve: {
      video: VideoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default videoRoute;
