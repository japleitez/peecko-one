import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { APS_PRICING_ACCESS, ApsPricingAccess, IApsPricing, NewApsPricing } from '../aps-pricing.model';

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
  country: FormControl<IApsPricing['country']>;
  customerId: FormControl<IApsPricing['customerId']>;
  index: FormControl<IApsPricing['index']>;
  minQuantity: FormControl<IApsPricing['minQuantity']>;
  unitPrice: FormControl<IApsPricing['unitPrice']>;
};

export type ApsPricingFormGroup = FormGroup<ApsPricingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsPricingFormService {
  createApsPricingFormGroup(apsPricing: ApsPricingFormGroupInput = { id: null }, ua: ApsPricingAccess = APS_PRICING_ACCESS): ApsPricingFormGroup {
    const apsPricingRawValue = {
      ...this.getFormDefaults(),
      ...apsPricing,
    };
    return new FormGroup<ApsPricingFormGroupContent>({
      id: new FormControl(
        { value: apsPricingRawValue.id, disabled: ua.id.disabled  },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      country: new FormControl({ value: apsPricingRawValue.country, disabled: ua.country.disabled  },
        { validators: [Validators.required],
      }),
      customerId: new FormControl({ value: apsPricingRawValue.customerId, disabled: ua.customerId.disabled  },
        { validators: [Validators.required],
      }),
      index: new FormControl({ value: apsPricingRawValue.index, disabled: ua.index.disabled  },
        { validators: [Validators.required],
      }),
      minQuantity: new FormControl({ value: apsPricingRawValue.minQuantity, disabled: ua.minQuantity.disabled  },
        { validators: [Validators.required],
      }),
      unitPrice: new FormControl({ value: apsPricingRawValue.unitPrice, disabled: ua.unitPrice.disabled  },
        { validators: [Validators.required],
      }),
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
