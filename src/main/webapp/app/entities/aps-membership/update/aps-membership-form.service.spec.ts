import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-membership.test-samples';

import { ApsMembershipFormService } from './aps-membership-form.service';

describe('ApsMembership Form Service', () => {
  let service: ApsMembershipFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsMembershipFormService);
  });

  describe('Service methods', () => {
    describe('createApsMembershipFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsMembershipFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            period: expect.any(Object),
            license: expect.any(Object),
            username: expect.any(Object),
            apsOrder: expect.any(Object),
          }),
        );
      });

      it('passing IApsMembership should create a new form with FormGroup', () => {
        const formGroup = service.createApsMembershipFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            period: expect.any(Object),
            license: expect.any(Object),
            username: expect.any(Object),
            apsOrder: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsMembership', () => {
      it('should return NewApsMembership for default ApsMembership initial value', () => {
        const formGroup = service.createApsMembershipFormGroup(sampleWithNewData);

        const apsMembership = service.getApsMembership(formGroup) as any;

        expect(apsMembership).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsMembership for empty ApsMembership initial value', () => {
        const formGroup = service.createApsMembershipFormGroup();

        const apsMembership = service.getApsMembership(formGroup) as any;

        expect(apsMembership).toMatchObject({});
      });

      it('should return IApsMembership', () => {
        const formGroup = service.createApsMembershipFormGroup(sampleWithRequiredData);

        const apsMembership = service.getApsMembership(formGroup) as any;

        expect(apsMembership).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsMembership should not enable id FormControl', () => {
        const formGroup = service.createApsMembershipFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsMembership should disable id FormControl', () => {
        const formGroup = service.createApsMembershipFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
