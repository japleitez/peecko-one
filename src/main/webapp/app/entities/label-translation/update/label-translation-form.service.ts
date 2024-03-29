import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILabelTranslation, LABEL_ACCESS, LabelAccess, NewLabelTranslation } from '../label-translation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILabelTranslation for edit and NewLabelTranslationFormGroupInput for create.
 */
type LabelTranslationFormGroupInput = ILabelTranslation | PartialWithRequiredKeyOf<NewLabelTranslation>;

type LabelTranslationFormDefaults = Pick<NewLabelTranslation, 'id'>;

type LabelTranslationFormGroupContent = {
  id: FormControl<ILabelTranslation['id'] | NewLabelTranslation['id']>;
  label: FormControl<ILabelTranslation['label']>;
  lang: FormControl<ILabelTranslation['lang']>;
  translation: FormControl<ILabelTranslation['translation']>;
};

export type LabelTranslationFormGroup = FormGroup<LabelTranslationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LabelTranslationFormService {
  createLabelTranslationFormGroup(
    labelTranslation: LabelTranslationFormGroupInput = { id: null },
    ua: LabelAccess = LABEL_ACCESS,
  ): LabelTranslationFormGroup {
    const labelTranslationRawValue = {
      ...this.getFormDefaults(),
      ...labelTranslation,
    };
    return new FormGroup<LabelTranslationFormGroupContent>({
      id: new FormControl(
        { value: labelTranslationRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl({ value: labelTranslationRawValue.label, disabled: ua.label.disabled }, { validators: [Validators.required] }),
      lang: new FormControl({ value: labelTranslationRawValue.lang, disabled: ua.lang.disabled }, { validators: [Validators.required] }),
      translation: new FormControl(
        { value: labelTranslationRawValue.translation, disabled: ua.translation.disabled },
        { validators: [Validators.required] },
      ),
    });
  }

  getLabelTranslation(form: LabelTranslationFormGroup): ILabelTranslation | NewLabelTranslation {
    return form.getRawValue() as ILabelTranslation | NewLabelTranslation;
  }

  resetForm(form: LabelTranslationFormGroup, labelTranslation: LabelTranslationFormGroupInput): void {
    const labelTranslationRawValue = { ...this.getFormDefaults(), ...labelTranslation };
    form.reset(
      {
        ...labelTranslationRawValue,
        id: { value: labelTranslationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LabelTranslationFormDefaults {
    return {
      id: null,
    };
  }
}
