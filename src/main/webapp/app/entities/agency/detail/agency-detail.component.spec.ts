import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgencyDetailComponent } from './agency-detail.component';

describe('Agency Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgencyDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AgencyDetailComponent,
              resolve: { agency: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AgencyDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load agency on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgencyDetailComponent);

      // THEN
      expect(instance.agency).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
