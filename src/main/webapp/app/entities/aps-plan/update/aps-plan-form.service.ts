import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { APS_PLAN_ACCESS, ApsPlanAccess, IApsPlan, NewApsPlan } from '../aps-plan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsPlan for edit and NewApsPlanFormGroupInput for create.
 */
type ApsPlanFormGroupInput = IApsPlan | PartialWithRequiredKeyOf<NewApsPlan>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IApsPlan | NewApsPlan> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type ApsPlanFormRawValue = FormValueOf<IApsPlan>;

type NewApsPlanFormRawValue = FormValueOf<NewApsPlan>;

type ApsPlanFormDefaults = Pick<NewApsPlan, 'id' | 'created' | 'updated'>;

type ApsPlanFormGroupContent = {
  id: FormControl<ApsPlanFormRawValue['id'] | NewApsPlan['id']>;
  contract: FormControl<ApsPlanFormRawValue['contract']>;
  pricing: FormControl<ApsPlanFormRawValue['pricing']>;
  state: FormControl<ApsPlanFormRawValue['state']>;
  license: FormControl<ApsPlanFormRawValue['license']>;
  starts: FormControl<ApsPlanFormRawValue['starts']>;
  ends: FormControl<ApsPlanFormRawValue['ends']>;
  unitPrice: FormControl<ApsPlanFormRawValue['unitPrice']>;
  notes: FormControl<ApsPlanFormRawValue['notes']>;
  created: FormControl<ApsPlanFormRawValue['created']>;
  updated: FormControl<ApsPlanFormRawValue['updated']>;
  customer: FormControl<ApsPlanFormRawValue['customer']>;
};

export type ApsPlanFormGroup = FormGroup<ApsPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsPlanFormService {
  createApsPlanFormGroup(apsPlan: ApsPlanFormGroupInput = { id: null }, ua: ApsPlanAccess =  APS_PLAN_ACCESS): ApsPlanFormGroup {
    const apsPlanRawValue = this.convertApsPlanToApsPlanRawValue({
      ...this.getFormDefaults(),
      ...apsPlan,
    });
    return new FormGroup<ApsPlanFormGroupContent>({
      id: new FormControl(
        { value: apsPlanRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contract: new FormControl({ value: apsPlanRawValue.contract, disabled: ua.contract.disabled },
        { validators: [Validators.required], }),
      pricing: new FormControl({ value: apsPlanRawValue.pricing, disabled: ua.pricing.disabled },
        { validators: [Validators.required], }),
      state: new FormControl({ value: apsPlanRawValue.state, disabled: ua.state.disabled },
        { validators: [Validators.required], }),
      license: new FormControl({ value: apsPlanRawValue.license, disabled: ua.license.disabled },
        { validators: [Validators.required], }),
      starts: new FormControl({ value: apsPlanRawValue.starts, disabled: ua.starts.disabled },
        { validators: [Validators.required], }),
      ends: new FormControl({ value: apsPlanRawValue.ends, disabled: ua.ends.disabled }),
      unitPrice: new FormControl({ value: apsPlanRawValue.unitPrice, disabled: ua.unitPrice.disabled },
        { validators: [Validators.required], }),
      notes: new FormControl({ value: apsPlanRawValue.notes, disabled: ua.notes.disabled }),
      created: new FormControl({ value: apsPlanRawValue.created, disabled: ua.created.disabled }),
      updated: new FormControl({ value: apsPlanRawValue.updated, disabled: ua.updated.disabled }),
      customer: new FormControl({ value: apsPlanRawValue.customer, disabled: ua.customer.disabled },
        { validators: [Validators.required], }),
    });
  }

  getApsPlan(form: ApsPlanFormGroup): IApsPlan | NewApsPlan {
    return this.convertApsPlanRawValueToApsPlan(form.getRawValue() as ApsPlanFormRawValue | NewApsPlanFormRawValue);
  }

  resetForm(form: ApsPlanFormGroup, apsPlan: ApsPlanFormGroupInput): void {
    const apsPlanRawValue = this.convertApsPlanToApsPlanRawValue({ ...this.getFormDefaults(), ...apsPlan });
    form.reset(
      {
        ...apsPlanRawValue,
        id: { value: apsPlanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsPlanFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertApsPlanRawValueToApsPlan(rawApsPlan: ApsPlanFormRawValue | NewApsPlanFormRawValue): IApsPlan | NewApsPlan {
    return {
      ...rawApsPlan,
      created: dayjs(rawApsPlan.created, DATE_TIME_FORMAT),
      updated: dayjs(rawApsPlan.updated, DATE_TIME_FORMAT),
    };
  }

  private convertApsPlanToApsPlanRawValue(
    apsPlan: IApsPlan | (Partial<NewApsPlan> & ApsPlanFormDefaults),
  ): ApsPlanFormRawValue | PartialWithRequiredKeyOf<NewApsPlanFormRawValue> {
    return {
      ...apsPlan,
      created: apsPlan.created ? apsPlan.created.format(DATE_TIME_FORMAT) : undefined,
      updated: apsPlan.updated ? apsPlan.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
