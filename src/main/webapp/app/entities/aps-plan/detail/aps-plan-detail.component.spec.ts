import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsPlanDetailComponent } from './aps-plan-detail.component';

describe('ApsPlan Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsPlanDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsPlanDetailComponent,
              resolve: { apsPlan: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsPlanDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsPlan on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsPlanDetailComponent);

      // THEN
      expect(instance.apsPlan).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
