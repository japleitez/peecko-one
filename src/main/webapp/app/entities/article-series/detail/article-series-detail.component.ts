import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ARTICLE_SERIES_ACCESS, ArticleSeriesAccess, IArticleSeries } from '../article-series.model';
import { ARTICLE_CATEGORY_ACCESS } from '../../article-category/article-category.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-article-series-detail',
  templateUrl: './article-series-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ArticleSeriesDetailComponent {
  ua: ArticleSeriesAccess = ARTICLE_SERIES_ACCESS;
  @Input() articleSeries: IArticleSeries | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
