import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LanguageService } from 'app/entities/language/service/language.service';
import { ARTICLE_SERIES_ACCESS, ArticleSeriesAccess, IArticleSeries } from '../article-series.model';
import { ArticleSeriesService } from '../service/article-series.service';
import { ArticleSeriesFormService, ArticleSeriesFormGroup } from './article-series-form.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-article-series-update',
  templateUrl: './article-series-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf],
})
export class ArticleSeriesUpdateComponent implements OnInit {
  ua: ArticleSeriesAccess = this.getArticleSeriesAccess();
  isSaving = false;
  articleSeries: IArticleSeries | null = null;
  languageValues: string[] = [];

  editForm: ArticleSeriesFormGroup = this.articleSeriesFormService.createArticleSeriesFormGroup();

  constructor(
    protected articleSeriesService: ArticleSeriesService,
    protected articleSeriesFormService: ArticleSeriesFormService,
    protected languageService: LanguageService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ articleSeries }) => {
      this.articleSeries = articleSeries;
      if (articleSeries) {
        this.updateForm(articleSeries);
      }

      this.loadLanguageCodes();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const articleSeries = this.articleSeriesFormService.getArticleSeries(this.editForm);
    if (articleSeries.id !== null) {
      this.subscribeToSaveResponse(this.articleSeriesService.update(articleSeries));
    } else {
      this.subscribeToSaveResponse(this.articleSeriesService.create(articleSeries));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticleSeries>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(articleSeries: IArticleSeries): void {
    this.articleSeries = articleSeries;
    this.articleSeriesFormService.resetForm(this.editForm, articleSeries);
  }

  protected loadLanguageCodes(): void {
    this.languageService
      .queryActiveCodes()
      .subscribe(codes => (this.languageValues = this.languageService.addLanguageCodeIfMissing(codes, this.articleSeries?.language)));
  }

  protected getArticleSeriesAccess(): ArticleSeriesAccess {
    return ARTICLE_SERIES_ACCESS;
  }
}
