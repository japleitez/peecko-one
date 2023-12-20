import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IApsPlan, NewApsPlan } from '../aps-plan.model';

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
  trialStarts: FormControl<ApsPlanFormRawValue['trialStarts']>;
  trialEnds: FormControl<ApsPlanFormRawValue['trialEnds']>;
  unitPrice: FormControl<ApsPlanFormRawValue['unitPrice']>;
  notes: FormControl<ApsPlanFormRawValue['notes']>;
  created: FormControl<ApsPlanFormRawValue['created']>;
  updated: FormControl<ApsPlanFormRawValue['updated']>;
  customer: FormControl<ApsPlanFormRawValue['customer']>;
};

export type ApsPlanFormGroup = FormGroup<ApsPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsPlanFormService {
  createApsPlanFormGroup(apsPlan: ApsPlanFormGroupInput = { id: null }): ApsPlanFormGroup {
    const apsPlanRawValue = this.convertApsPlanToApsPlanRawValue({
      ...this.getFormDefaults(),
      ...apsPlan,
    });
    return new FormGroup<ApsPlanFormGroupContent>({
      id: new FormControl(
        { value: apsPlanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contract: new FormControl(apsPlanRawValue.contract, {
        validators: [Validators.required],
      }),
      pricing: new FormControl(apsPlanRawValue.pricing, {
        validators: [Validators.required],
      }),
      state: new FormControl(apsPlanRawValue.state, {
        validators: [Validators.required],
      }),
      license: new FormControl(apsPlanRawValue.license),
      starts: new FormControl(apsPlanRawValue.starts),
      ends: new FormControl(apsPlanRawValue.ends),
      trialStarts: new FormControl(apsPlanRawValue.trialStarts),
      trialEnds: new FormControl(apsPlanRawValue.trialEnds),
      unitPrice: new FormControl(apsPlanRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      notes: new FormControl(apsPlanRawValue.notes),
      created: new FormControl(apsPlanRawValue.created),
      updated: new FormControl(apsPlanRawValue.updated),
      customer: new FormControl(apsPlanRawValue.customer),
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
