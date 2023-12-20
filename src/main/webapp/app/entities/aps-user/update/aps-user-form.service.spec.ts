import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-user.test-samples';

import { ApsUserFormService } from './aps-user-form.service';

describe('ApsUser Form Service', () => {
  let service: ApsUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsUserFormService);
  });

  describe('Service methods', () => {
    describe('createApsUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            username: expect.any(Object),
            usernameVerified: expect.any(Object),
            privateEmail: expect.any(Object),
            privateVerified: expect.any(Object),
            language: expect.any(Object),
            license: expect.any(Object),
            active: expect.any(Object),
            password: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
          }),
        );
      });

      it('passing IApsUser should create a new form with FormGroup', () => {
        const formGroup = service.createApsUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            username: expect.any(Object),
            usernameVerified: expect.any(Object),
            privateEmail: expect.any(Object),
            privateVerified: expect.any(Object),
            language: expect.any(Object),
            license: expect.any(Object),
            active: expect.any(Object),
            password: expect.any(Object),
            created: expect.any(Object),
            updated: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsUser', () => {
      it('should return NewApsUser for default ApsUser initial value', () => {
        const formGroup = service.createApsUserFormGroup(sampleWithNewData);

        const apsUser = service.getApsUser(formGroup) as any;

        expect(apsUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsUser for empty ApsUser initial value', () => {
        const formGroup = service.createApsUserFormGroup();

        const apsUser = service.getApsUser(formGroup) as any;

        expect(apsUser).toMatchObject({});
      });

      it('should return IApsUser', () => {
        const formGroup = service.createApsUserFormGroup(sampleWithRequiredData);

        const apsUser = service.getApsUser(formGroup) as any;

        expect(apsUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsUser should not enable id FormControl', () => {
        const formGroup = service.createApsUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsUser should disable id FormControl', () => {
        const formGroup = service.createApsUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
