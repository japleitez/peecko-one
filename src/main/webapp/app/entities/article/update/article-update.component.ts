import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IArticleCategory } from 'app/entities/article-category/article-category.model';
import { ArticleCategoryService } from 'app/entities/article-category/service/article-category.service';
import { ICoach } from 'app/entities/coach/coach.model';
import { CoachService } from 'app/entities/coach/service/coach.service';
import { Language } from 'app/entities/enumerations/language.model';
import { ArticleService } from '../service/article.service';
import { IArticle } from '../article.model';
import { ArticleFormService, ArticleFormGroup } from './article-form.service';

@Component({
  standalone: true,
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  article: IArticle | null = null;
  languageValues = Object.keys(Language);

  articleCategoriesSharedCollection: IArticleCategory[] = [];
  coachesSharedCollection: ICoach[] = [];

  editForm: ArticleFormGroup = this.articleFormService.createArticleFormGroup();

  constructor(
    protected articleService: ArticleService,
    protected articleFormService: ArticleFormService,
    protected articleCategoryService: ArticleCategoryService,
    protected coachService: CoachService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareArticleCategory = (o1: IArticleCategory | null, o2: IArticleCategory | null): boolean =>
    this.articleCategoryService.compareArticleCategory(o1, o2);

  compareCoach = (o1: ICoach | null, o2: ICoach | null): boolean => this.coachService.compareCoach(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.article = article;
      if (article) {
        this.updateForm(article);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.articleFormService.getArticle(this.editForm);
    if (article.id !== null) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  protected updateForm(article: IArticle): void {
    this.article = article;
    this.articleFormService.resetForm(this.editForm, article);

    this.articleCategoriesSharedCollection = this.articleCategoryService.addArticleCategoryToCollectionIfMissing<IArticleCategory>(
      this.articleCategoriesSharedCollection,
      article.articleCategory,
    );
    this.coachesSharedCollection = this.coachService.addCoachToCollectionIfMissing<ICoach>(this.coachesSharedCollection, article.coach);
  }

  protected loadRelationshipsOptions(): void {
    this.articleCategoryService
      .query()
      .pipe(map((res: HttpResponse<IArticleCategory[]>) => res.body ?? []))
      .pipe(
        map((articleCategories: IArticleCategory[]) =>
          this.articleCategoryService.addArticleCategoryToCollectionIfMissing<IArticleCategory>(
            articleCategories,
            this.article?.articleCategory,
          ),
        ),
      )
      .subscribe((articleCategories: IArticleCategory[]) => (this.articleCategoriesSharedCollection = articleCategories));

    this.coachService
      .query()
      .pipe(map((res: HttpResponse<ICoach[]>) => res.body ?? []))
      .pipe(map((coaches: ICoach[]) => this.coachService.addCoachToCollectionIfMissing<ICoach>(coaches, this.article?.coach)))
      .subscribe((coaches: ICoach[]) => (this.coachesSharedCollection = coaches));
  }
}
