import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICoach, NewCoach } from '../coach.model';

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
  createCoachFormGroup(coach: CoachFormGroupInput = { id: null }): CoachFormGroup {
    const coachRawValue = this.convertCoachToCoachRawValue({
      ...this.getFormDefaults(),
      ...coach,
    });
    return new FormGroup<CoachFormGroupContent>({
      id: new FormControl(
        { value: coachRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(coachRawValue.type, {
        validators: [Validators.required],
      }),
      name: new FormControl(coachRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(coachRawValue.email),
      website: new FormControl(coachRawValue.website),
      instagram: new FormControl(coachRawValue.instagram),
      phoneNumber: new FormControl(coachRawValue.phoneNumber),
      country: new FormControl(coachRawValue.country),
      speaks: new FormControl(coachRawValue.speaks),
      resume: new FormControl(coachRawValue.resume),
      notes: new FormControl(coachRawValue.notes),
      created: new FormControl(coachRawValue.created),
      updated: new FormControl(coachRawValue.updated),
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
