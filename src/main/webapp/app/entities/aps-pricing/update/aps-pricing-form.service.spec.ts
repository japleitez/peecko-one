import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-pricing.test-samples';

import { ApsPricingFormService } from './aps-pricing-form.service';

describe('ApsPricing Form Service', () => {
  let service: ApsPricingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsPricingFormService);
  });

  describe('Service methods', () => {
    describe('createApsPricingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsPricingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            country: expect.any(Object),
            customerId: expect.any(Object),
            index: expect.any(Object),
            minQuantity: expect.any(Object),
            unitPrice: expect.any(Object),
          }),
        );
      });

      it('passing IApsPricing should create a new form with FormGroup', () => {
        const formGroup = service.createApsPricingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            country: expect.any(Object),
            customerId: expect.any(Object),
            index: expect.any(Object),
            minQuantity: expect.any(Object),
            unitPrice: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsPricing', () => {
      it('should return NewApsPricing for default ApsPricing initial value', () => {
        const formGroup = service.createApsPricingFormGroup(sampleWithNewData);

        const apsPricing = service.getApsPricing(formGroup) as any;

        expect(apsPricing).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsPricing for empty ApsPricing initial value', () => {
        const formGroup = service.createApsPricingFormGroup();

        const apsPricing = service.getApsPricing(formGroup) as any;

        expect(apsPricing).toMatchObject({});
      });

      it('should return IApsPricing', () => {
        const formGroup = service.createApsPricingFormGroup(sampleWithRequiredData);

        const apsPricing = service.getApsPricing(formGroup) as any;

        expect(apsPricing).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsPricing should not enable id FormControl', () => {
        const formGroup = service.createApsPricingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsPricing should disable id FormControl', () => {
        const formGroup = service.createApsPricingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
