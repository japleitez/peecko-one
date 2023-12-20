import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArticleSeriesDetailComponent } from './article-series-detail.component';

describe('ArticleSeries Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleSeriesDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ArticleSeriesDetailComponent,
              resolve: { articleSeries: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ArticleSeriesDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load articleSeries on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ArticleSeriesDetailComponent);

      // THEN
      expect(instance.articleSeries).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
