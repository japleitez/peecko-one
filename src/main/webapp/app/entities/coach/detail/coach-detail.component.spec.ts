import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CoachDetailComponent } from './coach-detail.component';

describe('Coach Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoachDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CoachDetailComponent,
              resolve: { coach: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CoachDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load coach on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CoachDetailComponent);

      // THEN
      expect(instance.coach).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
