import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer.test-samples';

import { CustomerFormService } from './customer-form.service';

describe('Customer Form Service', () => {
  let service: CustomerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            country: expect.any(Object),
            membership: expect.any(Object),
            state: expect.any(Object),
            closeReason: expect.any(Object),
            emailDomains: expect.any(Object),
            vatId: expect.any(Object),
            bank: expect.any(Object),
            iban: expect.any(Object),
            logo: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            trialed: expect.any(Object),
            declined: expect.any(Object),
            activated: expect.any(Object),
            closed: expect.any(Object),
            agency: expect.any(Object),
          }),
        );
      });

      it('passing ICustomer should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            country: expect.any(Object),
            membership: expect.any(Object),
            state: expect.any(Object),
            closeReason: expect.any(Object),
            emailDomains: expect.any(Object),
            vatId: expect.any(Object),
            bank: expect.any(Object),
            iban: expect.any(Object),
            logo: expect.any(Object),
            notes: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
            trialed: expect.any(Object),
            declined: expect.any(Object),
            activated: expect.any(Object),
            closed: expect.any(Object),
            agency: expect.any(Object),
          }),
        );
      });
    });

    describe('getCustomer', () => {
      it('should return NewCustomer for default Customer initial value', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithNewData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomer for empty Customer initial value', () => {
        const formGroup = service.createCustomerFormGroup();

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject({});
      });

      it('should return ICustomer', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);

        const customer = service.getCustomer(formGroup) as any;

        expect(customer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomer should not enable id FormControl', () => {
        const formGroup = service.createCustomerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomer should disable id FormControl', () => {
        const formGroup = service.createCustomerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
