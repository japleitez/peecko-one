import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArticleCategoryService } from '../service/article-category.service';
import { IArticleCategory } from '../article-category.model';
import { ArticleCategoryFormService } from './article-category-form.service';

import { ArticleCategoryUpdateComponent } from './article-category-update.component';

describe('ArticleCategory Management Update Component', () => {
  let comp: ArticleCategoryUpdateComponent;
  let fixture: ComponentFixture<ArticleCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let articleCategoryFormService: ArticleCategoryFormService;
  let articleCategoryService: ArticleCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ArticleCategoryUpdateComponent],
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
      .overrideTemplate(ArticleCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    articleCategoryFormService = TestBed.inject(ArticleCategoryFormService);
    articleCategoryService = TestBed.inject(ArticleCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const articleCategory: IArticleCategory = { id: 456 };

      activatedRoute.data = of({ articleCategory });
      comp.ngOnInit();

      expect(comp.articleCategory).toEqual(articleCategory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleCategory>>();
      const articleCategory = { id: 123 };
      jest.spyOn(articleCategoryFormService, 'getArticleCategory').mockReturnValue(articleCategory);
      jest.spyOn(articleCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: articleCategory }));
      saveSubject.complete();

      // THEN
      expect(articleCategoryFormService.getArticleCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(articleCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(articleCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleCategory>>();
      const articleCategory = { id: 123 };
      jest.spyOn(articleCategoryFormService, 'getArticleCategory').mockReturnValue({ id: null });
      jest.spyOn(articleCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: articleCategory }));
      saveSubject.complete();

      // THEN
      expect(articleCategoryFormService.getArticleCategory).toHaveBeenCalled();
      expect(articleCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticleCategory>>();
      const articleCategory = { id: 123 };
      jest.spyOn(articleCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ articleCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(articleCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
