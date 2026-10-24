import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { ArticleDetailComponent } from './article-detail.component';

describe('Article Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ArticleDetailComponent,
              resolve: { article: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ArticleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load article on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ArticleDetailComponent);

      // THEN
      expect(instance.article).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

describe('Article Management Detail Component content rendering', () => {
  let fixture: ComponentFixture<ArticleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleDetailComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    TestBed.inject(FaIconLibrary).addIconPacks(fas);
    fixture = TestBed.createComponent(ArticleDetailComponent);
  });

  const renderContent = (content: string): string => {
    fixture.componentInstance.article = { id: 123, content };
    fixture.detectChanges();
    return (fixture.nativeElement as HTMLElement).querySelector('[data-cy="articleContent"]')!.innerHTML;
  };

  it('Should render the content as HTML instead of escaping it', () => {
    expect(renderContent('<p>Breathe <strong>slowly</strong></p>')).toBe('<p>Breathe <strong>slowly</strong></p>');
  });

  it('Should strip dangerous markup that reaches the browser', () => {
    const rendered = renderContent('<p>safe</p><script>alert("xss")</script><img src="x" onerror="alert(1)">');

    expect(rendered).toContain('<p>safe</p>');
    expect(rendered).not.toContain('script');
    expect(rendered).not.toContain('onerror');
  });
});
