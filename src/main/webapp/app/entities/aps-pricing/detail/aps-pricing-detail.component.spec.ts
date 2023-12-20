import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsPricingDetailComponent } from './aps-pricing-detail.component';

describe('ApsPricing Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsPricingDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsPricingDetailComponent,
              resolve: { apsPricing: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsPricingDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsPricing on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsPricingDetailComponent);

      // THEN
      expect(instance.apsPricing).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
