import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { TRIAL_LICENSE_ACCESS, TrialLicenseAccess, ITrialLicense, NewTrialLicense } from '../trial-license.model';

type PartialWithRequiredKeyOf<T extends { license: unknown }> = Partial<Omit<T, 'license'>> & { license: T['license'] };

type TrialLicenseFormGroupInput = ITrialLicense | PartialWithRequiredKeyOf<NewTrialLicense>;

type TrialLicenseFormGroupContent = {
  license: FormControl<ITrialLicense['license'] | NewTrialLicense['license']>;
  expirationDate: FormControl<ITrialLicense['expirationDate']>;
  notes: FormControl<ITrialLicense['notes']>;
};

export type TrialLicenseFormGroup = FormGroup<TrialLicenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrialLicenseFormService {
  createTrialLicenseFormGroup(
    trialLicense: TrialLicenseFormGroupInput = { license: null },
    ua: TrialLicenseAccess = TRIAL_LICENSE_ACCESS,
  ): TrialLicenseFormGroup {
    return new FormGroup<TrialLicenseFormGroupContent>({
      license: new FormControl(
        { value: trialLicense.license, disabled: ua.license.disabled },
        {
          nonNullable: true,
          validators: [Validators.required, Validators.maxLength(20)],
        },
      ),
      expirationDate: new FormControl(
        { value: trialLicense.expirationDate ?? null, disabled: ua.expirationDate.disabled },
        { validators: [Validators.required] },
      ),
      notes: new FormControl({ value: trialLicense.notes, disabled: ua.notes.disabled }, { validators: [Validators.required] }),
    });
  }

  getTrialLicense(form: TrialLicenseFormGroup): ITrialLicense {
    return form.getRawValue() as ITrialLicense;
  }

  resetForm(form: TrialLicenseFormGroup, trialLicense: TrialLicenseFormGroupInput): void {
    form.reset(
      {
        ...trialLicense,
        license: { value: trialLicense.license, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }
}
