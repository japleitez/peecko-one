import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ArticleComponent } from './list/article.component';
import { ArticleDetailComponent } from './detail/article-detail.component';
import { ArticleUpdateComponent } from './update/article-update.component';
import ArticleResolve from './route/article-routing-resolve.service';

const articleRoute: Routes = [
  {
    path: '',
    component: ArticleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArticleDetailComponent,
    resolve: {
      article: ArticleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArticleUpdateComponent,
    resolve: {
      article: ArticleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArticleUpdateComponent,
    resolve: {
      article: ArticleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default articleRoute;
