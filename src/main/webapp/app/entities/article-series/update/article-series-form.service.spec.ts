import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../article-series.test-samples';

import { ArticleSeriesFormService } from './article-series-form.service';

describe('ArticleSeries Form Service', () => {
  let service: ArticleSeriesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArticleSeriesFormService);
  });

  describe('Service methods', () => {
    describe('createArticleSeriesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArticleSeriesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            summary: expect.any(Object),
            language: expect.any(Object),
            tags: expect.any(Object),
            thumbnail: expect.any(Object),
            counter: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            released: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });

      it('passing IArticleSeries should create a new form with FormGroup', () => {
        const formGroup = service.createArticleSeriesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            summary: expect.any(Object),
            language: expect.any(Object),
            tags: expect.any(Object),
            thumbnail: expect.any(Object),
            counter: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            released: expect.any(Object),
            archived: expect.any(Object),
          }),
        );
      });
    });

    describe('getArticleSeries', () => {
      it('should return NewArticleSeries for default ArticleSeries initial value', () => {
        const formGroup = service.createArticleSeriesFormGroup(sampleWithNewData);

        const articleSeries = service.getArticleSeries(formGroup) as any;

        expect(articleSeries).toMatchObject(sampleWithNewData);
      });

      it('should return NewArticleSeries for empty ArticleSeries initial value', () => {
        const formGroup = service.createArticleSeriesFormGroup();

        const articleSeries = service.getArticleSeries(formGroup) as any;

        expect(articleSeries).toMatchObject({});
      });

      it('should return IArticleSeries', () => {
        const formGroup = service.createArticleSeriesFormGroup(sampleWithRequiredData);

        const articleSeries = service.getArticleSeries(formGroup) as any;

        expect(articleSeries).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArticleSeries should not enable id FormControl', () => {
        const formGroup = service.createArticleSeriesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArticleSeries should disable id FormControl', () => {
        const formGroup = service.createArticleSeriesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
