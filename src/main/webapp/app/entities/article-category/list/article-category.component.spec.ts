import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArticleCategoryService } from '../service/article-category.service';

import { ArticleCategoryComponent } from './article-category.component';

describe('ArticleCategory Management Component', () => {
  let comp: ArticleCategoryComponent;
  let fixture: ComponentFixture<ArticleCategoryComponent>;
  let service: ArticleCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'article-category', component: ArticleCategoryComponent }]),
        HttpClientTestingModule,
        ArticleCategoryComponent,
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
      .overrideTemplate(ArticleCategoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleCategoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ArticleCategoryService);

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
    expect(comp.articleCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to articleCategoryService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getArticleCategoryIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getArticleCategoryIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
