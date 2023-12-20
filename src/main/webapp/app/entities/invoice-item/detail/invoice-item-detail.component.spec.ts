import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceItemDetailComponent } from './invoice-item-detail.component';

describe('InvoiceItem Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceItemDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InvoiceItemDetailComponent,
              resolve: { invoiceItem: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InvoiceItemDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load invoiceItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InvoiceItemDetailComponent);

      // THEN
      expect(instance.invoiceItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
