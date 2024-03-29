import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ARTICLE_CATEGORY_ACCESS, ArticleCategoryAccess, IArticleCategory } from '../article-category.model';
import { ARTICLE_ACCESS } from '../../article/article.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-article-category-detail',
  templateUrl: './article-category-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ArticleCategoryDetailComponent {
  ua: ArticleCategoryAccess = ARTICLE_CATEGORY_ACCESS;
  @Input() articleCategory: IArticleCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
