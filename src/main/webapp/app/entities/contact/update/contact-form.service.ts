import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContact, NewContact } from '../contact.model';

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
  createContactFormGroup(contact: ContactFormGroupInput = { id: null }): ContactFormGroup {
    const contactRawValue = this.convertContactToContactRawValue({
      ...this.getFormDefaults(),
      ...contact,
    });
    return new FormGroup<ContactFormGroupContent>({
      id: new FormControl(
        { value: contactRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(contactRawValue.type, {
        validators: [Validators.required],
      }),
      name: new FormControl(contactRawValue.name, {
        validators: [Validators.required],
      }),
      line1: new FormControl(contactRawValue.line1),
      line2: new FormControl(contactRawValue.line2),
      zip: new FormControl(contactRawValue.zip),
      city: new FormControl(contactRawValue.city),
      country: new FormControl(contactRawValue.country),
      email: new FormControl(contactRawValue.email),
      phone: new FormControl(contactRawValue.phone),
      notes: new FormControl(contactRawValue.notes),
      created: new FormControl(contactRawValue.created),
      updated: new FormControl(contactRawValue.updated),
      customer: new FormControl(contactRawValue.customer),
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
