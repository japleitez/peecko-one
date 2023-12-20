import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ArticleCategoryComponent } from './list/article-category.component';
import { ArticleCategoryDetailComponent } from './detail/article-category-detail.component';
import { ArticleCategoryUpdateComponent } from './update/article-category-update.component';
import ArticleCategoryResolve from './route/article-category-routing-resolve.service';

const articleCategoryRoute: Routes = [
  {
    path: '',
    component: ArticleCategoryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArticleCategoryDetailComponent,
    resolve: {
      articleCategory: ArticleCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArticleCategoryUpdateComponent,
    resolve: {
      articleCategory: ArticleCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArticleCategoryUpdateComponent,
    resolve: {
      articleCategory: ArticleCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default articleCategoryRoute;
