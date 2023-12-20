import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice-item.test-samples';

import { InvoiceItemFormService } from './invoice-item-form.service';

describe('InvoiceItem Form Service', () => {
  let service: InvoiceItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceItemFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            quantity: expect.any(Object),
            priceUnit: expect.any(Object),
            priceExtended: expect.any(Object),
            disRate: expect.any(Object),
            disAmount: expect.any(Object),
            finalPrice: expect.any(Object),
            vatRate: expect.any(Object),
            vatAmount: expect.any(Object),
            total: expect.any(Object),
            invoice: expect.any(Object),
          }),
        );
      });

      it('passing IInvoiceItem should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            quantity: expect.any(Object),
            priceUnit: expect.any(Object),
            priceExtended: expect.any(Object),
            disRate: expect.any(Object),
            disAmount: expect.any(Object),
            finalPrice: expect.any(Object),
            vatRate: expect.any(Object),
            vatAmount: expect.any(Object),
            total: expect.any(Object),
            invoice: expect.any(Object),
          }),
        );
      });
    });

    describe('getInvoiceItem', () => {
      it('should return NewInvoiceItem for default InvoiceItem initial value', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithNewData);

        const invoiceItem = service.getInvoiceItem(formGroup) as any;

        expect(invoiceItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceItem for empty InvoiceItem initial value', () => {
        const formGroup = service.createInvoiceItemFormGroup();

        const invoiceItem = service.getInvoiceItem(formGroup) as any;

        expect(invoiceItem).toMatchObject({});
      });

      it('should return IInvoiceItem', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);

        const invoiceItem = service.getInvoiceItem(formGroup) as any;

        expect(invoiceItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceItem should not enable id FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceItem should disable id FormControl', () => {
        const formGroup = service.createInvoiceItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
