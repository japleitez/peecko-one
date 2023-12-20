import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ArticleSeriesComponent } from './list/article-series.component';
import { ArticleSeriesDetailComponent } from './detail/article-series-detail.component';
import { ArticleSeriesUpdateComponent } from './update/article-series-update.component';
import ArticleSeriesResolve from './route/article-series-routing-resolve.service';

const articleSeriesRoute: Routes = [
  {
    path: '',
    component: ArticleSeriesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArticleSeriesDetailComponent,
    resolve: {
      articleSeries: ArticleSeriesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArticleSeriesUpdateComponent,
    resolve: {
      articleSeries: ArticleSeriesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArticleSeriesUpdateComponent,
    resolve: {
      articleSeries: ArticleSeriesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default articleSeriesRoute;
