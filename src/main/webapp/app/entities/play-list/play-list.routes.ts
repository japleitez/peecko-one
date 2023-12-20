import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PlayListComponent } from './list/play-list.component';
import { PlayListDetailComponent } from './detail/play-list-detail.component';
import { PlayListUpdateComponent } from './update/play-list-update.component';
import PlayListResolve from './route/play-list-routing-resolve.service';

const playListRoute: Routes = [
  {
    path: '',
    component: PlayListComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayListDetailComponent,
    resolve: {
      playList: PlayListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayListUpdateComponent,
    resolve: {
      playList: PlayListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayListUpdateComponent,
    resolve: {
      playList: PlayListResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playListRoute;
