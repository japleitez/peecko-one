import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ARTICLE_CATEGORY_ACCESS, ArticleCategoryAccess, IArticleCategory } from '../article-category.model';
import { ArticleCategoryService } from '../service/article-category.service';
import { ArticleCategoryFormService, ArticleCategoryFormGroup } from './article-category-form.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-article-category-update',
  templateUrl: './article-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf]
})
export class ArticleCategoryUpdateComponent implements OnInit {
  ua: ArticleCategoryAccess = this.getArticleCategoryAccess();
  isSaving = false;
  articleCategory: IArticleCategory | null = null;

  editForm: ArticleCategoryFormGroup = this.articleCategoryFormService.createArticleCategoryFormGroup(undefined, this.getArticleCategoryAccess());

  constructor(
    protected articleCategoryService: ArticleCategoryService,
    protected articleCategoryFormService: ArticleCategoryFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ articleCategory }) => {
      this.articleCategory = articleCategory;
      if (articleCategory) {
        this.updateForm(articleCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const articleCategory = this.articleCategoryFormService.getArticleCategory(this.editForm);
    if (articleCategory.id !== null) {
      this.subscribeToSaveResponse(this.articleCategoryService.update(articleCategory));
    } else {
      this.subscribeToSaveResponse(this.articleCategoryService.create(articleCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticleCategory>>): void {
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

  protected updateForm(articleCategory: IArticleCategory): void {
    this.articleCategory = articleCategory;
    this.articleCategoryFormService.resetForm(this.editForm, articleCategory);
  }

  protected getArticleCategoryAccess(): ArticleCategoryAccess {
    return ARTICLE_CATEGORY_ACCESS;
  }

}
