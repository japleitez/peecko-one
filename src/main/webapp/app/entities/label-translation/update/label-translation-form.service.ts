import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILabelTranslation, NewLabelTranslation } from '../label-translation.model';

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
  createLabelTranslationFormGroup(labelTranslation: LabelTranslationFormGroupInput = { id: null }): LabelTranslationFormGroup {
    const labelTranslationRawValue = {
      ...this.getFormDefaults(),
      ...labelTranslation,
    };
    return new FormGroup<LabelTranslationFormGroupContent>({
      id: new FormControl(
        { value: labelTranslationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(labelTranslationRawValue.label, {
        validators: [Validators.required],
      }),
      lang: new FormControl(labelTranslationRawValue.lang, {
        validators: [Validators.required],
      }),
      translation: new FormControl(labelTranslationRawValue.translation, {
        validators: [Validators.required],
      }),
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
