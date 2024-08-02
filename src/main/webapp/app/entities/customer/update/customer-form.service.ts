import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CUSTOMER_USER_ACCESS, CustomerAccess, ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomer | NewCustomer> = Omit<T, 'created' | 'updated' | 'trialed' | 'declined' | 'activated' | 'closed'> & {
  created?: string | null;
  updated?: string | null;
  trialed?: string | null;
  declined?: string | null;
  activated?: string | null;
  closed?: string | null;
};

type CustomerFormRawValue = FormValueOf<ICustomer>;

type NewCustomerFormRawValue = FormValueOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'created' | 'updated' | 'trialed' | 'declined' | 'activated' | 'closed'>;

type CustomerFormGroupContent = {
  id: FormControl<CustomerFormRawValue['id'] | NewCustomer['id']>;
  code: FormControl<CustomerFormRawValue['code']>;
  name: FormControl<CustomerFormRawValue['name']>;
  country: FormControl<CustomerFormRawValue['country']>;
  license: FormControl<CustomerFormRawValue['license']>;
  state: FormControl<CustomerFormRawValue['state']>;
  closeReason: FormControl<CustomerFormRawValue['closeReason']>;
  billingEmail: FormControl<CustomerFormRawValue['billingEmail']>;
  vatId: FormControl<CustomerFormRawValue['vatId']>;
  vatRate: FormControl<CustomerFormRawValue['vatRate']>;
  bank: FormControl<CustomerFormRawValue['bank']>;
  iban: FormControl<CustomerFormRawValue['iban']>;
  logo: FormControl<CustomerFormRawValue['logo']>;
  notes: FormControl<CustomerFormRawValue['notes']>;
  created: FormControl<CustomerFormRawValue['created']>;
  updated: FormControl<CustomerFormRawValue['updated']>;
  trialed: FormControl<CustomerFormRawValue['trialed']>;
  declined: FormControl<CustomerFormRawValue['declined']>;
  activated: FormControl<CustomerFormRawValue['activated']>;
  closed: FormControl<CustomerFormRawValue['closed']>;
  agency: FormControl<CustomerFormRawValue['agency']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }, ua: CustomerAccess = CUSTOMER_USER_ACCESS): CustomerFormGroup {
    const customerRawValue = this.convertCustomerToCustomerRawValue({
      ...this.getFormDefaults(),
      ...customer,
    });
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl( { value: customerRawValue.code, disabled: ua.code.disabled },
      { validators: [Validators.required], }),
      name: new FormControl( { value: customerRawValue.name, disabled: ua.name.disabled },
        { validators: [Validators.required], }),
      country: new FormControl({ value: customerRawValue.country, disabled: ua.country.disabled },
        { validators: [Validators.required], }),
      license: new FormControl({ value: customerRawValue.license, disabled: ua.license.disabled }),
      state: new FormControl({ value: customerRawValue.state, disabled: ua.state.disabled },
        { validators: [Validators.required], }),
      closeReason: new FormControl({ value: customerRawValue.closeReason, disabled: ua.closeReason.disabled }),
      billingEmail: new FormControl({ value: customerRawValue.billingEmail, disabled: ua.billingEmail.disabled }),
      vatId: new FormControl({ value: customerRawValue.vatId, disabled: ua.vatId.disabled }),
      vatRate: new FormControl({ value: customerRawValue.vatRate, disabled: ua.vatRate.disabled },
        { validators: [Validators.required], }),
      bank: new FormControl({ value: customerRawValue.bank, disabled: ua.bank.disabled }),
      iban: new FormControl({ value: customerRawValue.iban, disabled: ua.iban.disabled }),
      logo: new FormControl({ value: customerRawValue.logo, disabled: ua.logo.disabled }),
      notes: new FormControl({ value: customerRawValue.notes, disabled: ua.notes.disabled }),
      created: new FormControl({ value: customerRawValue.created, disabled: ua.created.disabled }),
      updated: new FormControl({ value: customerRawValue.updated, disabled: ua.updated.disabled }),
      trialed: new FormControl({ value: customerRawValue.trialed, disabled: ua.trialed.disabled }),
      declined: new FormControl({ value: customerRawValue.declined, disabled: ua.declined.disabled }),
      activated: new FormControl({ value: customerRawValue.activated, disabled: ua.activated.disabled }),
      closed: new FormControl({ value: customerRawValue.closed, disabled: ua.closed.disabled }),
      agency: new FormControl({ value: customerRawValue.agency, disabled: ua.agency.disabled },
        { validators: [Validators.required], }),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return this.convertCustomerRawValueToCustomer(form.getRawValue() as CustomerFormRawValue | NewCustomerFormRawValue);
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = this.convertCustomerToCustomerRawValue({ ...this.getFormDefaults(), ...customer });
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: null,
      updated: null,
      trialed: null,
      declined: null,
      activated: null,
      closed: null,
    };
  }

  private convertCustomerRawValueToCustomer(rawCustomer: CustomerFormRawValue | NewCustomerFormRawValue): ICustomer | NewCustomer {
    return {
      ...rawCustomer,
      created: dayjs(rawCustomer.created, DATE_TIME_FORMAT),
      updated: dayjs(rawCustomer.updated, DATE_TIME_FORMAT),
      trialed: dayjs(rawCustomer.trialed, DATE_TIME_FORMAT),
      declined: dayjs(rawCustomer.declined, DATE_TIME_FORMAT),
      activated: dayjs(rawCustomer.activated, DATE_TIME_FORMAT),
      closed: dayjs(rawCustomer.closed, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerToCustomerRawValue(
    customer: ICustomer | (Partial<NewCustomer> & CustomerFormDefaults),
  ): CustomerFormRawValue | PartialWithRequiredKeyOf<NewCustomerFormRawValue> {
    return {
      ...customer,
      created: customer.created ? customer.created.format(DATE_TIME_FORMAT) : undefined,
      updated: customer.updated ? customer.updated.format(DATE_TIME_FORMAT) : undefined,
      trialed: customer.trialed ? customer.trialed.format(DATE_TIME_FORMAT) : undefined,
      declined: customer.declined ? customer.declined.format(DATE_TIME_FORMAT) : undefined,
      activated: customer.activated ? customer.activated.format(DATE_TIME_FORMAT) : undefined,
      closed: customer.closed ? customer.closed.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
