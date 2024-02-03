import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CONTACT_USER_ACCESS, ContactAccess, IContact, NewContact } from '../contact.model';
import { CustomerAccess } from '../../customer/customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContact for edit and NewContactFormGroupInput for create.
 */
type ContactFormGroupInput = IContact | PartialWithRequiredKeyOf<NewContact>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContact | NewContact> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type ContactFormRawValue = FormValueOf<IContact>;

type NewContactFormRawValue = FormValueOf<NewContact>;

type ContactFormDefaults = Pick<NewContact, 'id' | 'created' | 'updated'>;

type ContactFormGroupContent = {
  id: FormControl<ContactFormRawValue['id'] | NewContact['id']>;
  type: FormControl<ContactFormRawValue['type']>;
  name: FormControl<ContactFormRawValue['name']>;
  line1: FormControl<ContactFormRawValue['line1']>;
  line2: FormControl<ContactFormRawValue['line2']>;
  zip: FormControl<ContactFormRawValue['zip']>;
  city: FormControl<ContactFormRawValue['city']>;
  country: FormControl<ContactFormRawValue['country']>;
  email: FormControl<ContactFormRawValue['email']>;
  phone: FormControl<ContactFormRawValue['phone']>;
  notes: FormControl<ContactFormRawValue['notes']>;
  created: FormControl<ContactFormRawValue['created']>;
  updated: FormControl<ContactFormRawValue['updated']>;
  customer: FormControl<ContactFormRawValue['customer']>;
};

export type ContactFormGroup = FormGroup<ContactFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactFormService {
  createContactFormGroup(contact: ContactFormGroupInput = { id: null }, ua: ContactAccess = CONTACT_USER_ACCESS): ContactFormGroup {
    const contactRawValue = this.convertContactToContactRawValue({
      ...this.getFormDefaults(),
      ...contact,
    });
    return new FormGroup<ContactFormGroupContent>({
      id: new FormControl(
        { value: contactRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl( { value: contactRawValue.type, disabled: ua.type.disabled},
        { validators: [Validators.required],
      }),
      name: new FormControl({ value: contactRawValue.name, disabled: ua.name.disabled},
        { validators: [Validators.required],
      }),
      line1: new FormControl({ value: contactRawValue.line1, disabled: ua.line1.disabled}),
      line2: new FormControl({ value: contactRawValue.line2, disabled: ua.line2.disabled}),
      zip: new FormControl({ value: contactRawValue.zip, disabled: ua.zip.disabled}),
      city: new FormControl({ value: contactRawValue.city, disabled: ua.city.disabled}),
      country: new FormControl({ value: contactRawValue.country, disabled: ua.country.disabled}),
      email: new FormControl({ value: contactRawValue.email, disabled: ua.email.disabled}),
      phone: new FormControl({ value: contactRawValue.phone, disabled: ua.phone.disabled}),
      notes: new FormControl({ value: contactRawValue.notes, disabled: ua.notes.disabled}),
      created: new FormControl({ value: contactRawValue.created, disabled: ua.created.disabled}),
      updated: new FormControl({ value: contactRawValue.updated, disabled: ua.updated.disabled}),
      customer: new FormControl({ value: contactRawValue.customer, disabled: ua.customer.disabled}),
    });
  }

  getContact(form: ContactFormGroup): IContact | NewContact {
    return this.convertContactRawValueToContact(form.getRawValue() as ContactFormRawValue | NewContactFormRawValue);
  }

  resetForm(form: ContactFormGroup, contact: ContactFormGroupInput): void {
    const contactRawValue = this.convertContactToContactRawValue({ ...this.getFormDefaults(), ...contact });
    form.reset(
      {
        ...contactRawValue,
        id: { value: contactRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContactFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertContactRawValueToContact(rawContact: ContactFormRawValue | NewContactFormRawValue): IContact | NewContact {
    return {
      ...rawContact,
      created: dayjs(rawContact.created, DATE_TIME_FORMAT),
      updated: dayjs(rawContact.updated, DATE_TIME_FORMAT),
    };
  }

  private convertContactToContactRawValue(
    contact: IContact | (Partial<NewContact> & ContactFormDefaults),
  ): ContactFormRawValue | PartialWithRequiredKeyOf<NewContactFormRawValue> {
    return {
      ...contact,
      created: contact.created ? contact.created.format(DATE_TIME_FORMAT) : undefined,
      updated: contact.updated ? contact.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
