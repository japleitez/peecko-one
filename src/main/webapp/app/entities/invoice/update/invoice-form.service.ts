import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, INVOICE_ACCESS, InvoiceAccess, NewInvoice } from '../invoice.model';

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
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }, ua: InvoiceAccess = INVOICE_ACCESS): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      number: new FormControl({ value: invoiceRawValue.number, disabled: ua.number.disabled }, { validators: [Validators.required] }),
      issued: new FormControl({ value: invoiceRawValue.issued, disabled: ua.issued.disabled }, { validators: [Validators.required] }),
      dueDate: new FormControl({ value: invoiceRawValue.dueDate, disabled: ua.dueDate.disabled }, { validators: [Validators.required] }),
      saleDate: new FormControl({ value: invoiceRawValue.saleDate, disabled: ua.saleDate.disabled }, { validators: [Validators.required] }),
      subtotal: new FormControl({ value: invoiceRawValue.subtotal, disabled: ua.subtotal.disabled }, { validators: [Validators.required] }),
      vat: new FormControl({ value: invoiceRawValue.vat, disabled: ua.vat.disabled }, { validators: [Validators.required] }),
      total: new FormControl({ value: invoiceRawValue.total, disabled: ua.total.disabled }, { validators: [Validators.required] }),
      paid: new FormControl({ value: invoiceRawValue.paid, disabled: ua.paid.disabled }),
      paidDate: new FormControl({ value: invoiceRawValue.paidDate, disabled: ua.paidDate.disabled }),
      diff: new FormControl({ value: invoiceRawValue.diff, disabled: ua.diff.disabled }),
      notes: new FormControl({ value: invoiceRawValue.notes, disabled: ua.notes.disabled }),
      apsOrder: new FormControl({ value: invoiceRawValue.apsOrder, disabled: ua.apsOrder.disabled }),
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
