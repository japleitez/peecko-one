import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApsDeviceComponent } from './list/aps-device.component';
import { ApsDeviceDetailComponent } from './detail/aps-device-detail.component';
import { ApsDeviceUpdateComponent } from './update/aps-device-update.component';
import ApsDeviceResolve from './route/aps-device-routing-resolve.service';

const apsDeviceRoute: Routes = [
  {
    path: '',
    component: ApsDeviceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApsDeviceDetailComponent,
    resolve: {
      apsDevice: ApsDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApsDeviceUpdateComponent,
    resolve: {
      apsDevice: ApsDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApsDeviceUpdateComponent,
    resolve: {
      apsDevice: ApsDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default apsDeviceRoute;
