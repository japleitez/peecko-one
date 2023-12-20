import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LabelTranslationDetailComponent } from './label-translation-detail.component';

describe('LabelTranslation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LabelTranslationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LabelTranslationDetailComponent,
              resolve: { labelTranslation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LabelTranslationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load labelTranslation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LabelTranslationDetailComponent);

      // THEN
      expect(instance.labelTranslation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
