import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsUserDetailComponent } from './aps-user-detail.component';

describe('ApsUser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsUserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsUserDetailComponent,
              resolve: { apsUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsUserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsUserDetailComponent);

      // THEN
      expect(instance.apsUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
