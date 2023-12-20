import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agency.test-samples';

import { AgencyFormService } from './agency-form.service';

describe('Agency Form Service', () => {
  let service: AgencyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgencyFormService);
  });

  describe('Service methods', () => {
    describe('createAgencyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgencyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            line1: expect.any(Object),
            line2: expect.any(Object),
            zip: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            language: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            billingEmail: expect.any(Object),
            billingPhone: expect.any(Object),
            bank: expect.any(Object),
            iban: expect.any(Object),
            rcs: expect.any(Object),
            vatId: expect.any(Object),
            vatRate: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
          }),
        );
      });

      it('passing IAgency should create a new form with FormGroup', () => {
        const formGroup = service.createAgencyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            line1: expect.any(Object),
            line2: expect.any(Object),
            zip: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            language: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            billingEmail: expect.any(Object),
            billingPhone: expect.any(Object),
            bank: expect.any(Object),
            iban: expect.any(Object),
            rcs: expect.any(Object),
            vatId: expect.any(Object),
            vatRate: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
          }),
        );
      });
    });

    describe('getAgency', () => {
      it('should return NewAgency for default Agency initial value', () => {
        const formGroup = service.createAgencyFormGroup(sampleWithNewData);

        const agency = service.getAgency(formGroup) as any;

        expect(agency).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgency for empty Agency initial value', () => {
        const formGroup = service.createAgencyFormGroup();

        const agency = service.getAgency(formGroup) as any;

        expect(agency).toMatchObject({});
      });

      it('should return IAgency', () => {
        const formGroup = service.createAgencyFormGroup(sampleWithRequiredData);

        const agency = service.getAgency(formGroup) as any;

        expect(agency).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgency should not enable id FormControl', () => {
        const formGroup = service.createAgencyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgency should disable id FormControl', () => {
        const formGroup = service.createAgencyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
