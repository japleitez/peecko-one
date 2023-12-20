import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsDeviceDetailComponent } from './aps-device-detail.component';

describe('ApsDevice Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApsDeviceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ApsDeviceDetailComponent,
              resolve: { apsDevice: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ApsDeviceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load apsDevice on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ApsDeviceDetailComponent);

      // THEN
      expect(instance.apsDevice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
