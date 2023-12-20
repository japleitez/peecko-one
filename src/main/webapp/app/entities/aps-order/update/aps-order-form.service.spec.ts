import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-order.test-samples';

import { ApsOrderFormService } from './aps-order-form.service';

describe('ApsOrder Form Service', () => {
  let service: ApsOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsOrderFormService);
  });

  describe('Service methods', () => {
    describe('createApsOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            period: expect.any(Object),
            license: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRate: expect.any(Object),
            numberOfUsers: expect.any(Object),
            invoiceNumber: expect.any(Object),
            apsPlan: expect.any(Object),
          }),
        );
      });

      it('passing IApsOrder should create a new form with FormGroup', () => {
        const formGroup = service.createApsOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            period: expect.any(Object),
            license: expect.any(Object),
            unitPrice: expect.any(Object),
            vatRate: expect.any(Object),
            numberOfUsers: expect.any(Object),
            invoiceNumber: expect.any(Object),
            apsPlan: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsOrder', () => {
      it('should return NewApsOrder for default ApsOrder initial value', () => {
        const formGroup = service.createApsOrderFormGroup(sampleWithNewData);

        const apsOrder = service.getApsOrder(formGroup) as any;

        expect(apsOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsOrder for empty ApsOrder initial value', () => {
        const formGroup = service.createApsOrderFormGroup();

        const apsOrder = service.getApsOrder(formGroup) as any;

        expect(apsOrder).toMatchObject({});
      });

      it('should return IApsOrder', () => {
        const formGroup = service.createApsOrderFormGroup(sampleWithRequiredData);

        const apsOrder = service.getApsOrder(formGroup) as any;

        expect(apsOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsOrder should not enable id FormControl', () => {
        const formGroup = service.createApsOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsOrder should disable id FormControl', () => {
        const formGroup = service.createApsOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
