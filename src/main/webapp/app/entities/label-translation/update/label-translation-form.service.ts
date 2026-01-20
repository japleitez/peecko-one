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
  code: FormControl<ILabelTranslation['code']>;
  lang: FormControl<ILabelTranslation['lang']>;
  text: FormControl<ILabelTranslation['text']>;
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
      code: new FormControl({ value: labelTranslationRawValue.code, disabled: ua.code.disabled }, { validators: [Validators.required] }),
      lang: new FormControl({ value: labelTranslationRawValue.lang, disabled: ua.lang.disabled }, { validators: [Validators.required] }),
      text: new FormControl(
        { value: labelTranslationRawValue.text, disabled: ua.text.disabled },
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
