import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TrialLicenseComponent } from './list/trial-license.component';
import { TrialLicenseDetailComponent } from './detail/trial-license-detail.component';
import { TrialLicenseUpdateComponent } from './update/trial-license-update.component';
import TrialLicenseResolve from './route/trial-license-routing-resolve.service';

const trialLicenseRoute: Routes = [
  {
    path: '',
    component: TrialLicenseComponent,
    data: {
      defaultSort: 'license,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrialLicenseDetailComponent,
    resolve: {
      trialLicense: TrialLicenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrialLicenseUpdateComponent,
    resolve: {
      trialLicense: TrialLicenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrialLicenseUpdateComponent,
    resolve: {
      trialLicense: TrialLicenseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trialLicenseRoute;
