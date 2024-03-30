import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStaff, NewStaff, STAFF_ACCESS, StaffAccess } from '../staff.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStaff for edit and NewStaffFormGroupInput for create.
 */
type StaffFormGroupInput = IStaff | PartialWithRequiredKeyOf<NewStaff>;

type StaffFormDefaults = Pick<NewStaff, 'id'>;

type StaffFormGroupContent = {
  id: FormControl<IStaff['id'] | NewStaff['id']>;
  userId: FormControl<IStaff['userId']>;
  role: FormControl<IStaff['role']>;
  agency: FormControl<IStaff['agency']>;
};

export type StaffFormGroup = FormGroup<StaffFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StaffFormService {
  createStaffFormGroup(staff: StaffFormGroupInput = { id: null }, ua: StaffAccess = STAFF_ACCESS): StaffFormGroup {
    const staffRawValue = {
      ...this.getFormDefaults(),
      ...staff,
    };
    return new FormGroup<StaffFormGroupContent>({
      id: new FormControl(
        { value: staffRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl({ value: staffRawValue.userId, disabled: ua.userId.disabled },
        { validators: [Validators.required],
      }),
      role: new FormControl({ value: staffRawValue.role, disabled: ua.role.disabled },
        { validators: [Validators.required],
      }),
      agency: new FormControl({ value: staffRawValue.agency, disabled: ua.agency.disabled }),
    });
  }

  getStaff(form: StaffFormGroup): IStaff | NewStaff {
    return form.getRawValue() as IStaff | NewStaff;
  }

  resetForm(form: StaffFormGroup, staff: StaffFormGroupInput): void {
    const staffRawValue = { ...this.getFormDefaults(), ...staff };
    form.reset(
      {
        ...staffRawValue,
        id: { value: staffRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StaffFormDefaults {
    return {
      id: null,
    };
  }
}
