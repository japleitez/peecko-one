import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VideoItemComponent } from './list/video-item.component';
import { VideoItemDetailComponent } from './detail/video-item-detail.component';
import { VideoItemUpdateComponent } from './update/video-item-update.component';
import VideoItemResolve from './route/video-item-routing-resolve.service';

const videoItemRoute: Routes = [
  {
    path: '',
    component: VideoItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VideoItemDetailComponent,
    resolve: {
      videoItem: VideoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VideoItemUpdateComponent,
    resolve: {
      videoItem: VideoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VideoItemUpdateComponent,
    resolve: {
      videoItem: VideoItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default videoItemRoute;
