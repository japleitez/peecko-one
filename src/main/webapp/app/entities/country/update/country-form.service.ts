import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { COUNTRY_ACCESS, CountryAccess, ICountry, NewCountry } from '../country.model';

type PartialWithRequiredKeyOf<T extends { code: unknown }> = Partial<Omit<T, 'code'>> & { code: T['code'] };

type CountryFormGroupInput = ICountry | PartialWithRequiredKeyOf<NewCountry>;

type CountryFormGroupContent = {
  code: FormControl<ICountry['code'] | NewCountry['code']>;
  name: FormControl<ICountry['name']>;
  locale: FormControl<ICountry['locale']>;
  currency: FormControl<ICountry['currency']>;
  language: FormControl<ICountry['language']>;
};

export type CountryFormGroup = FormGroup<CountryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CountryFormService {
  createCountryFormGroup(country: CountryFormGroupInput = { code: null }, ua: CountryAccess = COUNTRY_ACCESS): CountryFormGroup {
    return new FormGroup<CountryFormGroupContent>({
      code: new FormControl(
        { value: country.code, disabled: ua.code.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl({ value: country.name ?? null, disabled: ua.name.disabled }, { validators: [Validators.required] }),
      locale: new FormControl({ value: country.locale ?? null, disabled: ua.locale.disabled }, { validators: [Validators.required] }),
      currency: new FormControl({ value: country.currency ?? null, disabled: ua.currency.disabled }, { validators: [Validators.required] }),
      language: new FormControl({ value: country.language ?? null, disabled: ua.language.disabled }, { validators: [Validators.required] }),
    });
  }

  getCountry(form: CountryFormGroup): ICountry {
    return form.getRawValue() as ICountry;
  }

  resetForm(form: CountryFormGroup, country: CountryFormGroupInput): void {
    form.reset(
      {
        ...country,
        code: { value: country.code, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }
}
