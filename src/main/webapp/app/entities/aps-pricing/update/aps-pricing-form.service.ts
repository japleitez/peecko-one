import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApsPricing, NewApsPricing } from '../aps-pricing.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsPricing for edit and NewApsPricingFormGroupInput for create.
 */
type ApsPricingFormGroupInput = IApsPricing | PartialWithRequiredKeyOf<NewApsPricing>;

type ApsPricingFormDefaults = Pick<NewApsPricing, 'id'>;

type ApsPricingFormGroupContent = {
  id: FormControl<IApsPricing['id'] | NewApsPricing['id']>;
  customerId: FormControl<IApsPricing['customerId']>;
  index: FormControl<IApsPricing['index']>;
  minQuantity: FormControl<IApsPricing['minQuantity']>;
  unitPrice: FormControl<IApsPricing['unitPrice']>;
  agency: FormControl<IApsPricing['agency']>;
};

export type ApsPricingFormGroup = FormGroup<ApsPricingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsPricingFormService {
  createApsPricingFormGroup(apsPricing: ApsPricingFormGroupInput = { id: null }): ApsPricingFormGroup {
    const apsPricingRawValue = {
      ...this.getFormDefaults(),
      ...apsPricing,
    };
    return new FormGroup<ApsPricingFormGroupContent>({
      id: new FormControl(
        { value: apsPricingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customerId: new FormControl(apsPricingRawValue.customerId, {
        validators: [Validators.required],
      }),
      index: new FormControl(apsPricingRawValue.index, {
        validators: [Validators.required],
      }),
      minQuantity: new FormControl(apsPricingRawValue.minQuantity, {
        validators: [Validators.required],
      }),
      unitPrice: new FormControl(apsPricingRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      agency: new FormControl(apsPricingRawValue.agency),
    });
  }

  getApsPricing(form: ApsPricingFormGroup): IApsPricing | NewApsPricing {
    return form.getRawValue() as IApsPricing | NewApsPricing;
  }

  resetForm(form: ApsPricingFormGroup, apsPricing: ApsPricingFormGroupInput): void {
    const apsPricingRawValue = { ...this.getFormDefaults(), ...apsPricing };
    form.reset(
      {
        ...apsPricingRawValue,
        id: { value: apsPricingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsPricingFormDefaults {
    return {
      id: null,
    };
  }
}
