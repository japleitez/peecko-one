import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAgency, NewAgency } from '../agency.model';

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
  createAgencyFormGroup(agency: AgencyFormGroupInput = { id: null }): AgencyFormGroup {
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
      code: new FormControl(agencyRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(agencyRawValue.name, {
        validators: [Validators.required],
      }),
      line1: new FormControl(agencyRawValue.line1),
      line2: new FormControl(agencyRawValue.line2),
      zip: new FormControl(agencyRawValue.zip),
      city: new FormControl(agencyRawValue.city, {
        validators: [Validators.required],
      }),
      country: new FormControl(agencyRawValue.country, {
        validators: [Validators.required],
      }),
      language: new FormControl(agencyRawValue.language, {
        validators: [Validators.required],
      }),
      email: new FormControl(agencyRawValue.email),
      phone: new FormControl(agencyRawValue.phone),
      billingEmail: new FormControl(agencyRawValue.billingEmail),
      billingPhone: new FormControl(agencyRawValue.billingPhone),
      bank: new FormControl(agencyRawValue.bank),
      iban: new FormControl(agencyRawValue.iban),
      rcs: new FormControl(agencyRawValue.rcs),
      vatId: new FormControl(agencyRawValue.vatId),
      vatRate: new FormControl(agencyRawValue.vatRate),
      notes: new FormControl(agencyRawValue.notes),
      created: new FormControl(agencyRawValue.created),
      updated: new FormControl(agencyRawValue.updated),
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
