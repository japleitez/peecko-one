import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoiceItem, NewInvoiceItem } from '../invoice-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItem for edit and NewInvoiceItemFormGroupInput for create.
 */
type InvoiceItemFormGroupInput = IInvoiceItem | PartialWithRequiredKeyOf<NewInvoiceItem>;

type InvoiceItemFormDefaults = Pick<NewInvoiceItem, 'id'>;

type InvoiceItemFormGroupContent = {
  id: FormControl<IInvoiceItem['id'] | NewInvoiceItem['id']>;
  type: FormControl<IInvoiceItem['type']>;
  description: FormControl<IInvoiceItem['description']>;
  quantity: FormControl<IInvoiceItem['quantity']>;
  priceUnit: FormControl<IInvoiceItem['priceUnit']>;
  priceExtended: FormControl<IInvoiceItem['priceExtended']>;
  disRate: FormControl<IInvoiceItem['disRate']>;
  disAmount: FormControl<IInvoiceItem['disAmount']>;
  finalPrice: FormControl<IInvoiceItem['finalPrice']>;
  vatRate: FormControl<IInvoiceItem['vatRate']>;
  vatAmount: FormControl<IInvoiceItem['vatAmount']>;
  total: FormControl<IInvoiceItem['total']>;
  invoice: FormControl<IInvoiceItem['invoice']>;
};

export type InvoiceItemFormGroup = FormGroup<InvoiceItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemFormService {
  createInvoiceItemFormGroup(invoiceItem: InvoiceItemFormGroupInput = { id: null }): InvoiceItemFormGroup {
    const invoiceItemRawValue = {
      ...this.getFormDefaults(),
      ...invoiceItem,
    };
    return new FormGroup<InvoiceItemFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(invoiceItemRawValue.type, {
        validators: [Validators.required],
      }),
      description: new FormControl(invoiceItemRawValue.description, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(invoiceItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      priceUnit: new FormControl(invoiceItemRawValue.priceUnit, {
        validators: [Validators.required],
      }),
      priceExtended: new FormControl(invoiceItemRawValue.priceExtended, {
        validators: [Validators.required],
      }),
      disRate: new FormControl(invoiceItemRawValue.disRate, {
        validators: [Validators.required],
      }),
      disAmount: new FormControl(invoiceItemRawValue.disAmount, {
        validators: [Validators.required],
      }),
      finalPrice: new FormControl(invoiceItemRawValue.finalPrice, {
        validators: [Validators.required],
      }),
      vatRate: new FormControl(invoiceItemRawValue.vatRate, {
        validators: [Validators.required],
      }),
      vatAmount: new FormControl(invoiceItemRawValue.vatAmount, {
        validators: [Validators.required],
      }),
      total: new FormControl(invoiceItemRawValue.total, {
        validators: [Validators.required],
      }),
      invoice: new FormControl(invoiceItemRawValue.invoice),
    });
  }

  getInvoiceItem(form: InvoiceItemFormGroup): IInvoiceItem | NewInvoiceItem {
    return form.getRawValue() as IInvoiceItem | NewInvoiceItem;
  }

  resetForm(form: InvoiceItemFormGroup, invoiceItem: InvoiceItemFormGroupInput): void {
    const invoiceItemRawValue = { ...this.getFormDefaults(), ...invoiceItem };
    form.reset(
      {
        ...invoiceItemRawValue,
        id: { value: invoiceItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceItemFormDefaults {
    return {
      id: null,
    };
  }
}
