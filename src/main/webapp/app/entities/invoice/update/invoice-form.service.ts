import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'issued'> & {
  issued?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'issued'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  number: FormControl<InvoiceFormRawValue['number']>;
  issued: FormControl<InvoiceFormRawValue['issued']>;
  dueDate: FormControl<InvoiceFormRawValue['dueDate']>;
  saleDate: FormControl<InvoiceFormRawValue['saleDate']>;
  subtotal: FormControl<InvoiceFormRawValue['subtotal']>;
  vat: FormControl<InvoiceFormRawValue['vat']>;
  total: FormControl<InvoiceFormRawValue['total']>;
  paid: FormControl<InvoiceFormRawValue['paid']>;
  paidDate: FormControl<InvoiceFormRawValue['paidDate']>;
  diff: FormControl<InvoiceFormRawValue['diff']>;
  notes: FormControl<InvoiceFormRawValue['notes']>;
  apsOrder: FormControl<InvoiceFormRawValue['apsOrder']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      number: new FormControl(invoiceRawValue.number, {
        validators: [Validators.required],
      }),
      issued: new FormControl(invoiceRawValue.issued, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(invoiceRawValue.dueDate, {
        validators: [Validators.required],
      }),
      saleDate: new FormControl(invoiceRawValue.saleDate, {
        validators: [Validators.required],
      }),
      subtotal: new FormControl(invoiceRawValue.subtotal, {
        validators: [Validators.required],
      }),
      vat: new FormControl(invoiceRawValue.vat, {
        validators: [Validators.required],
      }),
      total: new FormControl(invoiceRawValue.total, {
        validators: [Validators.required],
      }),
      paid: new FormControl(invoiceRawValue.paid),
      paidDate: new FormControl(invoiceRawValue.paidDate),
      diff: new FormControl(invoiceRawValue.diff),
      notes: new FormControl(invoiceRawValue.notes),
      apsOrder: new FormControl(invoiceRawValue.apsOrder),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issued: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      issued: dayjs(rawInvoice.issued, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      issued: invoice.issued ? invoice.issued.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
