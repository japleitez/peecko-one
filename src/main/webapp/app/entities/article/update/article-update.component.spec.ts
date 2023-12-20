import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IArticleCategory } from 'app/entities/article-category/article-category.model';
import { ArticleCategoryService } from 'app/entities/article-category/service/article-category.service';
import { ICoach } from 'app/entities/coach/coach.model';
import { CoachService } from 'app/entities/coach/service/coach.service';
import { IArticle } from '../article.model';
import { ArticleService } from '../service/article.service';
import { ArticleFormService } from './article-form.service';

import { ArticleUpdateComponent } from './article-update.component';

describe('Article Management Update Component', () => {
  let comp: ArticleUpdateComponent;
  let fixture: ComponentFixture<ArticleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let articleFormService: ArticleFormService;
  let articleService: ArticleService;
  let articleCategoryService: ArticleCategoryService;
  let coachService: CoachService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ArticleUpdateComponent],
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
      .overrideTemplate(ArticleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    articleFormService = TestBed.inject(ArticleFormService);
    articleService = TestBed.inject(ArticleService);
    articleCategoryService = TestBed.inject(ArticleCategoryService);
    coachService = TestBed.inject(CoachService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ArticleCategory query and add missing value', () => {
      const article: IArticle = { id: 456 };
      const articleCategory: IArticleCategory = { id: 19553 };
      article.articleCategory = articleCategory;

      const articleCategoryCollection: IArticleCategory[] = [{ id: 32364 }];
      jest.spyOn(articleCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCategoryCollection })));
      const additionalArticleCategories = [articleCategory];
      const expectedCollection: IArticleCategory[] = [...additionalArticleCategories, ...articleCategoryCollection];
      jest.spyOn(articleCategoryService, 'addArticleCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(articleCategoryService.query).toHaveBeenCalled();
      expect(articleCategoryService.addArticleCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        articleCategoryCollection,
        ...additionalArticleCategories.map(expect.objectContaining),
      );
      expect(comp.articleCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Coach query and add missing value', () => {
      const article: IArticle = { id: 456 };
      const coach: ICoach = { id: 10561 };
      article.coach = coach;

      const coachCollection: ICoach[] = [{ id: 21892 }];
      jest.spyOn(coachService, 'query').mockReturnValue(of(new HttpResponse({ body: coachCollection })));
      const additionalCoaches = [coach];
      const expectedCollection: ICoach[] = [...additionalCoaches, ...coachCollection];
      jest.spyOn(coachService, 'addCoachToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(coachService.query).toHaveBeenCalled();
      expect(coachService.addCoachToCollectionIfMissing).toHaveBeenCalledWith(
        coachCollection,
        ...additionalCoaches.map(expect.objectContaining),
      );
      expect(comp.coachesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const article: IArticle = { id: 456 };
      const articleCategory: IArticleCategory = { id: 5025 };
      article.articleCategory = articleCategory;
      const coach: ICoach = { id: 803 };
      article.coach = coach;

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(comp.articleCategoriesSharedCollection).toContain(articleCategory);
      expect(comp.coachesSharedCollection).toContain(coach);
      expect(comp.article).toEqual(article);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 123 };
      jest.spyOn(articleFormService, 'getArticle').mockReturnValue(article);
      jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: article }));
      saveSubject.complete();

      // THEN
      expect(articleFormService.getArticle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(articleService.update).toHaveBeenCalledWith(expect.objectContaining(article));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 123 };
      jest.spyOn(articleFormService, 'getArticle').mockReturnValue({ id: null });
      jest.spyOn(articleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: article }));
      saveSubject.complete();

      // THEN
      expect(articleFormService.getArticle).toHaveBeenCalled();
      expect(articleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 123 };
      jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(articleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareArticleCategory', () => {
      it('Should forward to articleCategoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(articleCategoryService, 'compareArticleCategory');
        comp.compareArticleCategory(entity, entity2);
        expect(articleCategoryService.compareArticleCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCoach', () => {
      it('Should forward to coachService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(coachService, 'compareCoach');
        comp.compareCoach(entity, entity2);
        expect(coachService.compareCoach).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
