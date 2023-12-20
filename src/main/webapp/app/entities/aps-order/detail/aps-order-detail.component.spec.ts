import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsOrderDetailComponent } from './aps-order-detail.component';

describe('ApsOrder Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsOrderDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsOrderDetailComponent,
              resolve: { apsOrder: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsOrderDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsOrder on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsOrderDetailComponent);

      // THEN
      expect(instance.apsOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
