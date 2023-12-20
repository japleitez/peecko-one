import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aps-device.test-samples';

import { ApsDeviceFormService } from './aps-device-form.service';

describe('ApsDevice Form Service', () => {
  let service: ApsDeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApsDeviceFormService);
  });

  describe('Service methods', () => {
    describe('createApsDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createApsDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            deviceId: expect.any(Object),
            phoneModel: expect.any(Object),
            osVersion: expect.any(Object),
            installedOn: expect.any(Object),
            apsUser: expect.any(Object),
          }),
        );
      });

      it('passing IApsDevice should create a new form with FormGroup', () => {
        const formGroup = service.createApsDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            deviceId: expect.any(Object),
            phoneModel: expect.any(Object),
            osVersion: expect.any(Object),
            installedOn: expect.any(Object),
            apsUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getApsDevice', () => {
      it('should return NewApsDevice for default ApsDevice initial value', () => {
        const formGroup = service.createApsDeviceFormGroup(sampleWithNewData);

        const apsDevice = service.getApsDevice(formGroup) as any;

        expect(apsDevice).toMatchObject(sampleWithNewData);
      });

      it('should return NewApsDevice for empty ApsDevice initial value', () => {
        const formGroup = service.createApsDeviceFormGroup();

        const apsDevice = service.getApsDevice(formGroup) as any;

        expect(apsDevice).toMatchObject({});
      });

      it('should return IApsDevice', () => {
        const formGroup = service.createApsDeviceFormGroup(sampleWithRequiredData);

        const apsDevice = service.getApsDevice(formGroup) as any;

        expect(apsDevice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApsDevice should not enable id FormControl', () => {
        const formGroup = service.createApsDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApsDevice should disable id FormControl', () => {
        const formGroup = service.createApsDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
