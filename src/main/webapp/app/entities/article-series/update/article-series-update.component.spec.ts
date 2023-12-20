import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArticleSeriesService } from '../service/article-series.service';
import { IArticleSeries } from '../article-series.model';
import { ArticleSeriesFormService } from './article-series-form.service';

import { ArticleSeriesUpdateComponent } from './article-series-update.component';

describe('ArticleSeries Management Update Component', () => {
  let comp: ArticleSeriesUpdateComponent;
  let fixture: ComponentFixture<ArticleSeriesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let articleSeriesFormService: ArticleSeriesFormService;
  let articleSeriesService: ArticleSeriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ArticleSeriesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ArticleSeriesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleSeriesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    articleSeriesFormService = TestBed.inject(ArticleSeriesFormService);
    articleSeriesService = TestBed.inject(ArticleSeriesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const articleSeries: IArticleSeries = { id: 456 };

      activatedRoute.data = of({ articleSeries });
      comp.ngOnInit();

      expect(comp.articleSeries).toEqual(articleSeries);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleSeries>>();
      const articleSeries = { id: 123 };
      jest.spyOn(articleSeriesFormService, 'getArticleSeries').mockReturnValue(articleSeries);
      jest.spyOn(articleSeriesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleSeries });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: articleSeries }));
      saveSubject.complete();

      // THEN
      expect(articleSeriesFormService.getArticleSeries).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(articleSeriesService.update).toHaveBeenCalledWith(expect.objectContaining(articleSeries));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleSeries>>();
      const articleSeries = { id: 123 };
      jest.spyOn(articleSeriesFormService, 'getArticleSeries').mockReturnValue({ id: null });
      jest.spyOn(articleSeriesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleSeries: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: articleSeries }));
      saveSubject.complete();

      // THEN
      expect(articleSeriesFormService.getArticleSeries).toHaveBeenCalled();
      expect(articleSeriesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleSeries>>();
      const articleSeries = { id: 123 };
      jest.spyOn(articleSeriesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleSeries });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(articleSeriesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
