import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoiceItem, INVOICE_ITEM_ACCESS, InvoiceItemAccess, NewInvoiceItem } from '../invoice-item.model';

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
  createInvoiceItemFormGroup(invoiceItem: InvoiceItemFormGroupInput = { id: null }, ua: InvoiceItemAccess = INVOICE_ITEM_ACCESS): InvoiceItemFormGroup {
    const invoiceItemRawValue = {
      ...this.getFormDefaults(),
      ...invoiceItem,
    };
    return new FormGroup<InvoiceItemFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl({ value: invoiceItemRawValue.type, disabled: ua.type.disabled },
        { validators: [Validators.required],
      }),
      description: new FormControl({ value: invoiceItemRawValue.description, disabled: ua.description.disabled },
        { validators: [Validators.required],
      }),
      quantity: new FormControl({ value: invoiceItemRawValue.quantity, disabled: ua.quantity.disabled },
        { validators: [Validators.required],
      }),
      priceUnit: new FormControl({ value: invoiceItemRawValue.priceUnit, disabled: ua.priceUnit.disabled },
        { validators: [Validators.required],
      }),
      priceExtended: new FormControl({ value: invoiceItemRawValue.priceExtended, disabled: ua.priceExtended.disabled },
        { validators: [Validators.required],
      }),
      disRate: new FormControl({ value: invoiceItemRawValue.disRate, disabled: ua.disRate.disabled },
        { validators: [Validators.required],
      }),
      disAmount: new FormControl({ value: invoiceItemRawValue.disAmount, disabled: ua.disAmount.disabled },
        { validators: [Validators.required],
      }),
      finalPrice: new FormControl({ value: invoiceItemRawValue.finalPrice, disabled: ua.finalPrice.disabled },
        { validators: [Validators.required],
      }),
      vatRate: new FormControl({ value: invoiceItemRawValue.vatRate, disabled: ua.vatRate.disabled },
        { validators: [Validators.required],
      }),
      vatAmount: new FormControl({ value: invoiceItemRawValue.vatAmount, disabled: ua.vatAmount.disabled },
        { validators: [Validators.required],
      }),
      total: new FormControl({ value: invoiceItemRawValue.total, disabled: ua.total.disabled },
        { validators: [Validators.required],
      }),
      invoice: new FormControl({ value: invoiceItemRawValue.invoice, disabled: ua.invoice.disabled }),
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
