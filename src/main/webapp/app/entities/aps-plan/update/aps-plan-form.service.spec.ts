import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-plan.test-samples';

import { ApsPlanFormService } from './aps-plan-form.service';

describe('ApsPlan Form Service', () => {
  let service: ApsPlanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsPlanFormService);
  });

  describe('Service methods', () => {
    describe('createApsPlanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsPlanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contract: expect.any(Object),
            pricing: expect.any(Object),
            state: expect.any(Object),
            license: expect.any(Object),
            starts: expect.any(Object),
            ends: expect.any(Object),
            trialStarts: expect.any(Object),
            trialEnds: expect.any(Object),
            unitPrice: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IApsPlan should create a new form with FormGroup', () => {
        const formGroup = service.createApsPlanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contract: expect.any(Object),
            pricing: expect.any(Object),
            state: expect.any(Object),
            license: expect.any(Object),
            starts: expect.any(Object),
            ends: expect.any(Object),
            trialStarts: expect.any(Object),
            trialEnds: expect.any(Object),
            unitPrice: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsPlan', () => {
      it('should return NewApsPlan for default ApsPlan initial value', () => {
        const formGroup = service.createApsPlanFormGroup(sampleWithNewData);

        const apsPlan = service.getApsPlan(formGroup) as any;

        expect(apsPlan).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsPlan for empty ApsPlan initial value', () => {
        const formGroup = service.createApsPlanFormGroup();

        const apsPlan = service.getApsPlan(formGroup) as any;

        expect(apsPlan).toMatchObject({});
      });

      it('should return IApsPlan', () => {
        const formGroup = service.createApsPlanFormGroup(sampleWithRequiredData);

        const apsPlan = service.getApsPlan(formGroup) as any;

        expect(apsPlan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsPlan should not enable id FormControl', () => {
        const formGroup = service.createApsPlanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsPlan should disable id FormControl', () => {
        const formGroup = service.createApsPlanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
