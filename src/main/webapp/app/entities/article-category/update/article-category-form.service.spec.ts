import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../article-category.test-samples';

import { ArticleCategoryFormService } from './article-category-form.service';

describe('ArticleCategory Form Service', () => {
  let service: ArticleCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArticleCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createArticleCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArticleCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            label: expect.any(Object),
            created: expect.any(Object),
            release: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });

      it('passing IArticleCategory should create a new form with FormGroup', () => {
        const formGroup = service.createArticleCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            label: expect.any(Object),
            created: expect.any(Object),
            release: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });
    });

    describe('getArticleCategory', () => {
      it('should return NewArticleCategory for default ArticleCategory initial value', () => {
        const formGroup = service.createArticleCategoryFormGroup(sampleWithNewData);

        const articleCategory = service.getArticleCategory(formGroup) as any;

        expect(articleCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewArticleCategory for empty ArticleCategory initial value', () => {
        const formGroup = service.createArticleCategoryFormGroup();

        const articleCategory = service.getArticleCategory(formGroup) as any;

        expect(articleCategory).toMatchObject({});
      });

      it('should return IArticleCategory', () => {
        const formGroup = service.createArticleCategoryFormGroup(sampleWithRequiredData);

        const articleCategory = service.getArticleCategory(formGroup) as any;

        expect(articleCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArticleCategory should not enable id FormControl', () => {
        const formGroup = service.createArticleCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArticleCategory should disable id FormControl', () => {
        const formGroup = service.createArticleCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
