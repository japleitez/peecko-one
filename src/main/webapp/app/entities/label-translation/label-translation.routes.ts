import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LabelTranslationComponent } from './list/label-translation.component';
import { LabelTranslationDetailComponent } from './detail/label-translation-detail.component';
import { LabelTranslationUpdateComponent } from './update/label-translation-update.component';
import LabelTranslationResolve from './route/label-translation-routing-resolve.service';

const labelTranslationRoute: Routes = [
  {
    path: '',
    component: LabelTranslationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LabelTranslationDetailComponent,
    resolve: {
      labelTranslation: LabelTranslationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LabelTranslationUpdateComponent,
    resolve: {
      labelTranslation: LabelTranslationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LabelTranslationUpdateComponent,
    resolve: {
      labelTranslation: LabelTranslationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default labelTranslationRoute;
