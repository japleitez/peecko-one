import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArticleService } from '../service/article.service';

import { ArticleComponent } from './article.component';
import SpyInstance = jest.SpyInstance;

describe('Article Management Component', () => {
  let comp: ArticleComponent;
  let fixture: ComponentFixture<ArticleComponent>;
  let service: ArticleService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'article', component: ArticleComponent }]),
        HttpClientTestingModule,
        ArticleComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ArticleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ArticleService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.articles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to articleService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getArticleIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getArticleIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should load a page', () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.navigateToWithComponentValues();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['name,asc'],
        }),
      }),
    );
  });
  it('should send the selected category to the backend', () => {
    // GIVEN
    comp.articleCategoryId = 42;

    // WHEN
    comp['queryBackend'](1, 'id', true).subscribe();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ articleCategoryId: 42 }));
  });

  it('should go back to the first page and keep the category in the URL when the category changes', () => {
    // GIVEN
    comp.page = 3;
    comp.articleCategoryId = 7;

    // WHEN
    comp.onArticleCategoryChange();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({ page: 1, articleCategoryId: 7 }),
      }),
    );
  });

  it('should remove the category and language from the URL when the filters are reset', () => {
    // GIVEN
    comp.articleCategoryId = 7;
    comp.language = 'FR';

    // WHEN
    comp.resetSearchForm();

    // THEN
    expect(comp.articleCategoryId).toBeNull();
    expect(comp.language).toBeNull();
    const navigationExtras = routerNavigateSpy.mock.lastCall![1];
    expect(navigationExtras?.queryParams).not.toHaveProperty('articleCategoryId');
    expect(navigationExtras?.queryParams).not.toHaveProperty('language');
  });

  it('should send the selected language to the backend', () => {
    // GIVEN
    comp.language = 'EN';

    // WHEN
    comp['queryBackend'](1, 'id', true).subscribe();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ language: 'EN' }));
  });

  it('should go back to the first page and keep the language in the URL when the language changes', () => {
    // GIVEN
    comp.page = 3;
    comp.language = 'DE';

    // WHEN
    comp.onLanguageChange();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({ page: 1, language: 'DE' }),
      }),
    );
  });
});
