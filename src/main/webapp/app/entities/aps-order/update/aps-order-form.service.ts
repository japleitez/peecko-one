import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApsOrder, NewApsOrder } from '../aps-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApsOrder for edit and NewApsOrderFormGroupInput for create.
 */
type ApsOrderFormGroupInput = IApsOrder | PartialWithRequiredKeyOf<NewApsOrder>;

type ApsOrderFormDefaults = Pick<NewApsOrder, 'id'>;

type ApsOrderFormGroupContent = {
  id: FormControl<IApsOrder['id'] | NewApsOrder['id']>;
  period: FormControl<IApsOrder['period']>;
  license: FormControl<IApsOrder['license']>;
  unitPrice: FormControl<IApsOrder['unitPrice']>;
  vatRate: FormControl<IApsOrder['vatRate']>;
  numberOfUsers: FormControl<IApsOrder['numberOfUsers']>;
  invoiceNumber: FormControl<IApsOrder['invoiceNumber']>;
  apsPlan: FormControl<IApsOrder['apsPlan']>;
};

export type ApsOrderFormGroup = FormGroup<ApsOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApsOrderFormService {
  createApsOrderFormGroup(apsOrder: ApsOrderFormGroupInput = { id: null }): ApsOrderFormGroup {
    const apsOrderRawValue = {
      ...this.getFormDefaults(),
      ...apsOrder,
    };
    return new FormGroup<ApsOrderFormGroupContent>({
      id: new FormControl(
        { value: apsOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      period: new FormControl(apsOrderRawValue.period, {
        validators: [Validators.required],
      }),
      license: new FormControl(apsOrderRawValue.license, {
        validators: [Validators.required],
      }),
      unitPrice: new FormControl(apsOrderRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      vatRate: new FormControl(apsOrderRawValue.vatRate, {
        validators: [Validators.required],
      }),
      numberOfUsers: new FormControl(apsOrderRawValue.numberOfUsers, {
        validators: [Validators.required],
      }),
      invoiceNumber: new FormControl(apsOrderRawValue.invoiceNumber),
      apsPlan: new FormControl(apsOrderRawValue.apsPlan),
    });
  }

  getApsOrder(form: ApsOrderFormGroup): IApsOrder | NewApsOrder {
    return form.getRawValue() as IApsOrder | NewApsOrder;
  }

  resetForm(form: ApsOrderFormGroup, apsOrder: ApsOrderFormGroupInput): void {
    const apsOrderRawValue = { ...this.getFormDefaults(), ...apsOrder };
    form.reset(
      {
        ...apsOrderRawValue,
        id: { value: apsOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ApsOrderFormDefaults {
    return {
      id: null,
    };
  }
}
