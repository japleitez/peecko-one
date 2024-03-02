import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { APS_USER_ACCESS, ApsUserAccess, IApsUser, NewApsUser } from '../aps-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsUser for edit and NewApsUserFormGroupInput for create.
 */
type ApsUserFormGroupInput = IApsUser | PartialWithRequiredKeyOf<NewApsUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApsUser | NewApsUser> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type ApsUserFormRawValue = FormValueOf<IApsUser>;

type NewApsUserFormRawValue = FormValueOf<NewApsUser>;

type ApsUserFormDefaults = Pick<NewApsUser, 'id' | 'usernameVerified' | 'privateVerified' | 'active' | 'created' | 'updated'>;

type ApsUserFormGroupContent = {
  id: FormControl<ApsUserFormRawValue['id'] | NewApsUser['id']>;
  name: FormControl<ApsUserFormRawValue['name']>;
  username: FormControl<ApsUserFormRawValue['username']>;
  usernameVerified: FormControl<ApsUserFormRawValue['usernameVerified']>;
  privateEmail: FormControl<ApsUserFormRawValue['privateEmail']>;
  privateVerified: FormControl<ApsUserFormRawValue['privateVerified']>;
  language: FormControl<ApsUserFormRawValue['language']>;
  license: FormControl<ApsUserFormRawValue['license']>;
  active: FormControl<ApsUserFormRawValue['active']>;
  password: FormControl<ApsUserFormRawValue['password']>;
  created: FormControl<ApsUserFormRawValue['created']>;
  updated: FormControl<ApsUserFormRawValue['updated']>;
};

export type ApsUserFormGroup = FormGroup<ApsUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsUserFormService {
  createApsUserFormGroup(apsUser: ApsUserFormGroupInput = { id: null }, ua: ApsUserAccess = APS_USER_ACCESS): ApsUserFormGroup {
    const apsUserRawValue = this.convertApsUserToApsUserRawValue({
      ...this.getFormDefaults(),
      ...apsUser,
    });
    return new FormGroup<ApsUserFormGroupContent>({
      id: new FormControl(
        { value: apsUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl({ value: apsUserRawValue.name, disabled: ua.name.disabled },
        { validators: [Validators.required], }),
      username: new FormControl({ value: apsUserRawValue.username, disabled: ua.username.disabled },
        { validators: [Validators.required], }),
      usernameVerified: new FormControl({ value: apsUserRawValue.usernameVerified, disabled: ua.usernameVerified.disabled },
        { validators: [Validators.required], }),
      privateEmail: new FormControl({ value: apsUserRawValue.privateEmail, disabled: ua.privateEmail.disabled },
        { validators: [Validators.required], }),
      privateVerified: new FormControl({ value: apsUserRawValue.privateVerified, disabled: ua.privateVerified.disabled },
        { validators: [Validators.required], }),
      language: new FormControl({ value: apsUserRawValue.language, disabled: ua.language.disabled },
        { validators: [Validators.required], }),
      license: new FormControl({ value: apsUserRawValue.license, disabled: ua.license.disabled }),
      active: new FormControl({ value: apsUserRawValue.active, disabled: ua.active.disabled },
        { validators: [Validators.required], }),
      password: new FormControl({ value: apsUserRawValue.password, disabled: ua.password.disabled }),
      created: new FormControl({ value: apsUserRawValue.created, disabled: ua.created.disabled }),
      updated: new FormControl({ value: apsUserRawValue.updated, disabled: ua.updated.disabled }),
    });
  }

  getApsUser(form: ApsUserFormGroup): IApsUser | NewApsUser {
    return this.convertApsUserRawValueToApsUser(form.getRawValue() as ApsUserFormRawValue | NewApsUserFormRawValue);
  }

  resetForm(form: ApsUserFormGroup, apsUser: ApsUserFormGroupInput): void {
    const apsUserRawValue = this.convertApsUserToApsUserRawValue({ ...this.getFormDefaults(), ...apsUser });
    form.reset(
      {
        ...apsUserRawValue,
        id: { value: apsUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      usernameVerified: false,
      privateVerified: false,
      active: false,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertApsUserRawValueToApsUser(rawApsUser: ApsUserFormRawValue | NewApsUserFormRawValue): IApsUser | NewApsUser {
    return {
      ...rawApsUser,
      created: dayjs(rawApsUser.created, DATE_TIME_FORMAT),
      updated: dayjs(rawApsUser.updated, DATE_TIME_FORMAT),
    };
  }

  private convertApsUserToApsUserRawValue(
    apsUser: IApsUser | (Partial<NewApsUser> & ApsUserFormDefaults),
  ): ApsUserFormRawValue | PartialWithRequiredKeyOf<NewApsUserFormRawValue> {
    return {
      ...apsUser,
      created: apsUser.created ? apsUser.created.format(DATE_TIME_FORMAT) : undefined,
      updated: apsUser.updated ? apsUser.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
