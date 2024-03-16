import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { APS_DEVICE_ACCESS, ApsDeviceAccess, IApsDevice, NewApsDevice } from '../aps-device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsDevice for edit and NewApsDeviceFormGroupInput for create.
 */
type ApsDeviceFormGroupInput = IApsDevice | PartialWithRequiredKeyOf<NewApsDevice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApsDevice | NewApsDevice> = Omit<T, 'installedOn'> & {
  installedOn?: string | null;
};

type ApsDeviceFormRawValue = FormValueOf<IApsDevice>;

type NewApsDeviceFormRawValue = FormValueOf<NewApsDevice>;

type ApsDeviceFormDefaults = Pick<NewApsDevice, 'id' | 'installedOn'>;

type ApsDeviceFormGroupContent = {
  id: FormControl<ApsDeviceFormRawValue['id'] | NewApsDevice['id']>;
  username: FormControl<ApsDeviceFormRawValue['username']>;
  deviceId: FormControl<ApsDeviceFormRawValue['deviceId']>;
  phoneModel: FormControl<ApsDeviceFormRawValue['phoneModel']>;
  osVersion: FormControl<ApsDeviceFormRawValue['osVersion']>;
  installedOn: FormControl<ApsDeviceFormRawValue['installedOn']>;
  apsUser: FormControl<ApsDeviceFormRawValue['apsUser']>;
};

export type ApsDeviceFormGroup = FormGroup<ApsDeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsDeviceFormService {
  createApsDeviceFormGroup(apsDevice: ApsDeviceFormGroupInput = { id: null }, ua: ApsDeviceAccess = APS_DEVICE_ACCESS): ApsDeviceFormGroup {
    const apsDeviceRawValue = this.convertApsDeviceToApsDeviceRawValue({
      ...this.getFormDefaults(),
      ...apsDevice,
    });
    return new FormGroup<ApsDeviceFormGroupContent>({
      id: new FormControl(
        { value: apsDeviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl({ value: apsDeviceRawValue.username, disabled: ua.username.disabled },
        { validators: [Validators.required], }),
      deviceId: new FormControl({ value: apsDeviceRawValue.deviceId, disabled: ua.deviceId.disabled },
        { validators: [Validators.required], }),
      phoneModel: new FormControl({ value: apsDeviceRawValue.phoneModel, disabled: ua.phoneModel.disabled }),
      osVersion: new FormControl({ value: apsDeviceRawValue.osVersion, disabled: ua.osVersion.disabled }),
      installedOn: new FormControl({ value: apsDeviceRawValue.installedOn, disabled: ua.installedOn.disabled }),
      apsUser: new FormControl({ value: apsDeviceRawValue.apsUser, disabled: ua.apsUser.disabled }),
    });
  }

  getApsDevice(form: ApsDeviceFormGroup): IApsDevice | NewApsDevice {
    return this.convertApsDeviceRawValueToApsDevice(form.getRawValue() as ApsDeviceFormRawValue | NewApsDeviceFormRawValue);
  }

  resetForm(form: ApsDeviceFormGroup, apsDevice: ApsDeviceFormGroupInput): void {
    const apsDeviceRawValue = this.convertApsDeviceToApsDeviceRawValue({ ...this.getFormDefaults(), ...apsDevice });
    form.reset(
      {
        ...apsDeviceRawValue,
        id: { value: apsDeviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsDeviceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      installedOn: currentTime,
    };
  }

  private convertApsDeviceRawValueToApsDevice(rawApsDevice: ApsDeviceFormRawValue | NewApsDeviceFormRawValue): IApsDevice | NewApsDevice {
    return {
      ...rawApsDevice,
      installedOn: dayjs(rawApsDevice.installedOn, DATE_TIME_FORMAT),
    };
  }

  private convertApsDeviceToApsDeviceRawValue(
    apsDevice: IApsDevice | (Partial<NewApsDevice> & ApsDeviceFormDefaults),
  ): ApsDeviceFormRawValue | PartialWithRequiredKeyOf<NewApsDeviceFormRawValue> {
    return {
      ...apsDevice,
      installedOn: apsDevice.installedOn ? apsDevice.installedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
