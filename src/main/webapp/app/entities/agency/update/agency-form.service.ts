import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { AGENCY_USER_ACCESS, AgencyAccess, IAgency, NewAgency } from '../agency.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgency for edit and NewAgencyFormGroupInput for create.
 */
type AgencyFormGroupInput = IAgency | PartialWithRequiredKeyOf<NewAgency>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAgency | NewAgency> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type AgencyFormRawValue = FormValueOf<IAgency>;

type NewAgencyFormRawValue = FormValueOf<NewAgency>;

type AgencyFormDefaults = Pick<NewAgency, 'id' | 'created' | 'updated'>;

type AgencyFormGroupContent = {
  id: FormControl<AgencyFormRawValue['id'] | NewAgency['id']>;
  code: FormControl<AgencyFormRawValue['code']>;
  name: FormControl<AgencyFormRawValue['name']>;
  line1: FormControl<AgencyFormRawValue['line1']>;
  line2: FormControl<AgencyFormRawValue['line2']>;
  zip: FormControl<AgencyFormRawValue['zip']>;
  city: FormControl<AgencyFormRawValue['city']>;
  country: FormControl<AgencyFormRawValue['country']>;
  language: FormControl<AgencyFormRawValue['language']>;
  email: FormControl<AgencyFormRawValue['email']>;
  phone: FormControl<AgencyFormRawValue['phone']>;
  billingEmail: FormControl<AgencyFormRawValue['billingEmail']>;
  billingPhone: FormControl<AgencyFormRawValue['billingPhone']>;
  bank: FormControl<AgencyFormRawValue['bank']>;
  iban: FormControl<AgencyFormRawValue['iban']>;
  rcs: FormControl<AgencyFormRawValue['rcs']>;
  vatId: FormControl<AgencyFormRawValue['vatId']>;
  vatRate: FormControl<AgencyFormRawValue['vatRate']>;
  notes: FormControl<AgencyFormRawValue['notes']>;
  created: FormControl<AgencyFormRawValue['created']>;
  updated: FormControl<AgencyFormRawValue['updated']>;
};

export type AgencyFormGroup = FormGroup<AgencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgencyFormService {
  createAgencyFormGroup(agency: AgencyFormGroupInput = { id: null }, ua: AgencyAccess = AGENCY_USER_ACCESS): AgencyFormGroup {
    const agencyRawValue = this.convertAgencyToAgencyRawValue({
      ...this.getFormDefaults(),
      ...agency,
    });
    return new FormGroup<AgencyFormGroupContent>({
      id: new FormControl(
        { value: agencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl( { value: agencyRawValue.code, disabled: ua.code.disabled },
        { validators: [Validators.required],
      }),
      name: new FormControl({ value: agencyRawValue.name, disabled: ua.name.disabled },
        { validators: [Validators.required],
      }),
      line1: new FormControl({ value: agencyRawValue.line1, disabled: ua.line1.disabled }),
      line2: new FormControl({ value: agencyRawValue.line2, disabled: ua.line2.disabled }),
      zip: new FormControl({ value: agencyRawValue.zip, disabled: ua.zip.disabled }),
      city: new FormControl({ value: agencyRawValue.city, disabled: ua.city.disabled },
        { validators: [Validators.required],
      }),
      country: new FormControl({ value: agencyRawValue.country, disabled: ua.country.disabled },
        { validators: [Validators.required],
      }),
      language: new FormControl({ value: agencyRawValue.language, disabled: ua.language.disabled },
        { validators: [Validators.required],
      }),
      email: new FormControl({ value: agencyRawValue.email, disabled: ua.email.disabled }),
      phone: new FormControl({ value: agencyRawValue.phone, disabled: ua.phone.disabled }),
      billingEmail: new FormControl({ value: agencyRawValue.billingEmail, disabled: ua.billingEmail.disabled }),
      billingPhone: new FormControl({ value: agencyRawValue.billingPhone, disabled: ua.billingPhone.disabled }),
      bank: new FormControl({ value: agencyRawValue.bank, disabled: ua.bank.disabled }),
      iban: new FormControl({ value: agencyRawValue.iban, disabled: ua.iban.disabled }),
      rcs: new FormControl({ value: agencyRawValue.rcs, disabled: ua.rcs.disabled }),
      vatId: new FormControl({ value: agencyRawValue.vatId, disabled: ua.vatId.disabled }),
      vatRate: new FormControl({ value: agencyRawValue.vatRate, disabled: ua.vatRate.disabled }),
      notes: new FormControl({ value: agencyRawValue.notes, disabled: ua.notes.disabled }),
      created: new FormControl({ value: agencyRawValue.created, disabled: true }),
      updated: new FormControl({ value: agencyRawValue.updated, disabled: true }),
    });
  }

  getAgency(form: AgencyFormGroup): IAgency | NewAgency {
    return this.convertAgencyRawValueToAgency(form.getRawValue() as AgencyFormRawValue | NewAgencyFormRawValue);
  }

  resetForm(form: AgencyFormGroup, agency: AgencyFormGroupInput): void {
    const agencyRawValue = this.convertAgencyToAgencyRawValue({ ...this.getFormDefaults(), ...agency });
    form.reset(
      {
        ...agencyRawValue,
        id: { value: agencyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgencyFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertAgencyRawValueToAgency(rawAgency: AgencyFormRawValue | NewAgencyFormRawValue): IAgency | NewAgency {
    return {
      ...rawAgency,
      created: dayjs(rawAgency.created, DATE_TIME_FORMAT),
      updated: dayjs(rawAgency.updated, DATE_TIME_FORMAT),
    };
  }

  private convertAgencyToAgencyRawValue(
    agency: IAgency | (Partial<NewAgency> & AgencyFormDefaults),
  ): AgencyFormRawValue | PartialWithRequiredKeyOf<NewAgencyFormRawValue> {
    return {
      ...agency,
      created: agency.created ? agency.created.format(DATE_TIME_FORMAT) : undefined,
      updated: agency.updated ? agency.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
