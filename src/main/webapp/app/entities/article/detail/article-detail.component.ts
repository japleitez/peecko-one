import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ARTICLE_ACCESS, ArticleAccess, IArticle } from '../article.model';
import { APS_PLAN_ACCESS } from '../../aps-plan/aps-plan.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-article-detail',
  templateUrl: './article-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ArticleDetailComponent {
  ua: ArticleAccess = ARTICLE_ACCESS;
  @Input() article: IArticle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
