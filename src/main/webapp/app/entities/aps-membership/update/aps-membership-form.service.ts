import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import {
  APS_MEMBERSHIP_USER_ACCESS,
  ApsMembershipAccess,
  IApsMembership,
  NewApsMembership
} from '../aps-membership.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsMembership for edit and NewApsMembershipFormGroupInput for create.
 */
type ApsMembershipFormGroupInput = IApsMembership | PartialWithRequiredKeyOf<NewApsMembership>;

type ApsMembershipFormDefaults = Pick<NewApsMembership, 'id'>;

type ApsMembershipFormGroupContent = {
  id: FormControl<IApsMembership['id'] | NewApsMembership['id']>;
  period: FormControl<IApsMembership['period']>;
  license: FormControl<IApsMembership['license']>;
  username: FormControl<IApsMembership['username']>;
  apsOrder: FormControl<IApsMembership['apsOrder']>;
};

export type ApsMembershipFormGroup = FormGroup<ApsMembershipFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsMembershipFormService {
  createApsMembershipFormGroup(apsMembership: ApsMembershipFormGroupInput = { id: null }, ua: ApsMembershipAccess = APS_MEMBERSHIP_USER_ACCESS): ApsMembershipFormGroup {
    const apsMembershipRawValue = {
      ...this.getFormDefaults(),
      ...apsMembership,
    };
    return new FormGroup<ApsMembershipFormGroupContent>({
      id: new FormControl(
        { value: apsMembershipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      period: new FormControl({ value: apsMembershipRawValue.period, disabled: ua.period.disabled },
        { validators: [Validators.required],
      }),
      license: new FormControl({ value: apsMembershipRawValue.license, disabled: ua.license.disabled },
        { validators: [Validators.required],
      }),
      username: new FormControl({ value: apsMembershipRawValue.username, disabled: ua.username.disabled },
        { validators: [Validators.required],
      }),
      apsOrder: new FormControl({ value: apsMembershipRawValue.apsOrder, disabled: ua.apsOrder.disabled }),
    });
  }

  getApsMembership(form: ApsMembershipFormGroup): IApsMembership | NewApsMembership {
    return form.getRawValue() as IApsMembership | NewApsMembership;
  }

  resetForm(form: ApsMembershipFormGroup, apsMembership: ApsMembershipFormGroupInput): void {
    const apsMembershipRawValue = { ...this.getFormDefaults(), ...apsMembership };
    form.reset(
      {
        ...apsMembershipRawValue,
        id: { value: apsMembershipRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsMembershipFormDefaults {
    return {
      id: null,
    };
  }
}
