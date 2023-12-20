import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsMembershipDetailComponent } from './aps-membership-detail.component';

describe('ApsMembership Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsMembershipDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsMembershipDetailComponent,
              resolve: { apsMembership: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsMembershipDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsMembership on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsMembershipDetailComponent);

      // THEN
      expect(instance.apsMembership).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
