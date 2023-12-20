import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomer, NewCustomer } from '../customer.model';

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
  membership: FormControl<CustomerFormRawValue['membership']>;
  state: FormControl<CustomerFormRawValue['state']>;
  closeReason: FormControl<CustomerFormRawValue['closeReason']>;
  emailDomains: FormControl<CustomerFormRawValue['emailDomains']>;
  vatId: FormControl<CustomerFormRawValue['vatId']>;
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
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
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
      code: new FormControl(customerRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(customerRawValue.name, {
        validators: [Validators.required],
      }),
      country: new FormControl(customerRawValue.country, {
        validators: [Validators.required],
      }),
      membership: new FormControl(customerRawValue.membership),
      state: new FormControl(customerRawValue.state, {
        validators: [Validators.required],
      }),
      closeReason: new FormControl(customerRawValue.closeReason),
      emailDomains: new FormControl(customerRawValue.emailDomains),
      vatId: new FormControl(customerRawValue.vatId),
      bank: new FormControl(customerRawValue.bank),
      iban: new FormControl(customerRawValue.iban),
      logo: new FormControl(customerRawValue.logo),
      notes: new FormControl(customerRawValue.notes),
      created: new FormControl(customerRawValue.created),
      updated: new FormControl(customerRawValue.updated),
      trialed: new FormControl(customerRawValue.trialed),
      declined: new FormControl(customerRawValue.declined),
      activated: new FormControl(customerRawValue.activated),
      closed: new FormControl(customerRawValue.closed),
      agency: new FormControl(customerRawValue.agency),
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
      created: currentTime,
      updated: currentTime,
      trialed: currentTime,
      declined: currentTime,
      activated: currentTime,
      closed: currentTime,
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
