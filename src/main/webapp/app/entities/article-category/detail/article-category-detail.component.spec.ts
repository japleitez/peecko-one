import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArticleCategoryDetailComponent } from './article-category-detail.component';

describe('ArticleCategory Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleCategoryDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ArticleCategoryDetailComponent,
              resolve: { articleCategory: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ArticleCategoryDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load articleCategory on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ArticleCategoryDetailComponent);

      // THEN
      expect(instance.articleCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
