import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { LANGUAGE_ACCESS, LanguageAccess, ILanguage, NewLanguage } from '../language.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILanguage for edit and NewLanguageFormGroupInput for create.
 */
type LanguageFormGroupInput = ILanguage | PartialWithRequiredKeyOf<NewLanguage>;

type LanguageFormDefaults = Pick<NewLanguage, 'id' | 'active'>;

type LanguageFormGroupContent = {
  id: FormControl<ILanguage['id'] | NewLanguage['id']>;
  code: FormControl<ILanguage['code']>;
  name: FormControl<ILanguage['name']>;
  active: FormControl<ILanguage['active']>;
};

export type LanguageFormGroup = FormGroup<LanguageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LanguageFormService {
  createLanguageFormGroup(language: LanguageFormGroupInput = { id: null }, ua: LanguageAccess = LANGUAGE_ACCESS): LanguageFormGroup {
    const languageRawValue = {
      ...this.getFormDefaults(),
      ...language,
    };
    return new FormGroup<LanguageFormGroupContent>({
      id: new FormControl(
        { value: languageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(
        { value: languageRawValue.code ?? null, disabled: ua.code.disabled },
        { validators: [Validators.required, Validators.maxLength(2)] },
      ),
      name: new FormControl(
        { value: languageRawValue.name ?? null, disabled: ua.name.disabled },
        { validators: [Validators.required, Validators.maxLength(50)] },
      ),
      active: new FormControl(
        { value: languageRawValue.active ?? false, disabled: ua.active.disabled },
        { validators: [Validators.required] },
      ),
    });
  }

  getLanguage(form: LanguageFormGroup): ILanguage | NewLanguage {
    return form.getRawValue() as ILanguage | NewLanguage;
  }

  resetForm(form: LanguageFormGroup, language: LanguageFormGroupInput): void {
    const languageRawValue = { ...this.getFormDefaults(), ...language };
    form.reset(
      {
        ...languageRawValue,
        id: { value: languageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LanguageFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
