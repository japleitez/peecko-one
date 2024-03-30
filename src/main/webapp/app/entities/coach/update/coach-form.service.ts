import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { COACH_ACCESS, CoachAccess, ICoach, NewCoach } from '../coach.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICoach for edit and NewCoachFormGroupInput for create.
 */
type CoachFormGroupInput = ICoach | PartialWithRequiredKeyOf<NewCoach>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICoach | NewCoach> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type CoachFormRawValue = FormValueOf<ICoach>;

type NewCoachFormRawValue = FormValueOf<NewCoach>;

type CoachFormDefaults = Pick<NewCoach, 'id' | 'created' | 'updated'>;

type CoachFormGroupContent = {
  id: FormControl<CoachFormRawValue['id'] | NewCoach['id']>;
  type: FormControl<CoachFormRawValue['type']>;
  name: FormControl<CoachFormRawValue['name']>;
  email: FormControl<CoachFormRawValue['email']>;
  website: FormControl<CoachFormRawValue['website']>;
  instagram: FormControl<CoachFormRawValue['instagram']>;
  phoneNumber: FormControl<CoachFormRawValue['phoneNumber']>;
  country: FormControl<CoachFormRawValue['country']>;
  speaks: FormControl<CoachFormRawValue['speaks']>;
  resume: FormControl<CoachFormRawValue['resume']>;
  notes: FormControl<CoachFormRawValue['notes']>;
  created: FormControl<CoachFormRawValue['created']>;
  updated: FormControl<CoachFormRawValue['updated']>;
};

export type CoachFormGroup = FormGroup<CoachFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CoachFormService {
  createCoachFormGroup(coach: CoachFormGroupInput = { id: null }, ua: CoachAccess = COACH_ACCESS): CoachFormGroup {
    const coachRawValue = this.convertCoachToCoachRawValue({
      ...this.getFormDefaults(),
      ...coach,
    });
    return new FormGroup<CoachFormGroupContent>({
      id: new FormControl(
        { value: coachRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl({ value: coachRawValue.type, disabled: ua.type.disabled }, { validators: [Validators.required] }),
      name: new FormControl({ value: coachRawValue.name, disabled: ua.name.disabled }, { validators: [Validators.required] }),
      email: new FormControl({ value: coachRawValue.email, disabled: ua.email.disabled }),
      website: new FormControl({ value: coachRawValue.website, disabled: ua.website.disabled }),
      instagram: new FormControl({ value: coachRawValue.instagram, disabled: ua.instagram.disabled }),
      phoneNumber: new FormControl({ value: coachRawValue.phoneNumber, disabled: ua.phoneNumber.disabled }),
      country: new FormControl({ value: coachRawValue.country, disabled: ua.country.disabled }),
      speaks: new FormControl({ value: coachRawValue.speaks, disabled: ua.speaks.disabled }),
      resume: new FormControl({ value: coachRawValue.resume, disabled: ua.resume.disabled }),
      notes: new FormControl({ value: coachRawValue.notes, disabled: ua.notes.disabled }),
      created: new FormControl({ value: coachRawValue.created, disabled: ua.created.disabled }),
      updated: new FormControl({ value: coachRawValue.updated, disabled: ua.updated.disabled }),
    });
  }

  getCoach(form: CoachFormGroup): ICoach | NewCoach {
    return this.convertCoachRawValueToCoach(form.getRawValue() as CoachFormRawValue | NewCoachFormRawValue);
  }

  resetForm(form: CoachFormGroup, coach: CoachFormGroupInput): void {
    const coachRawValue = this.convertCoachToCoachRawValue({ ...this.getFormDefaults(), ...coach });
    form.reset(
      {
        ...coachRawValue,
        id: { value: coachRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CoachFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertCoachRawValueToCoach(rawCoach: CoachFormRawValue | NewCoachFormRawValue): ICoach | NewCoach {
    return {
      ...rawCoach,
      created: dayjs(rawCoach.created, DATE_TIME_FORMAT),
      updated: dayjs(rawCoach.updated, DATE_TIME_FORMAT),
    };
  }

  private convertCoachToCoachRawValue(
    coach: ICoach | (Partial<NewCoach> & CoachFormDefaults),
  ): CoachFormRawValue | PartialWithRequiredKeyOf<NewCoachFormRawValue> {
    return {
      ...coach,
      created: coach.created ? coach.created.format(DATE_TIME_FORMAT) : undefined,
      updated: coach.updated ? coach.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
