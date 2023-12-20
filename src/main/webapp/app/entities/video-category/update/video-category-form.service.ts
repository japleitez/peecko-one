import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVideoCategory, NewVideoCategory } from '../video-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVideoCategory for edit and NewVideoCategoryFormGroupInput for create.
 */
type VideoCategoryFormGroupInput = IVideoCategory | PartialWithRequiredKeyOf<NewVideoCategory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVideoCategory | NewVideoCategory> = Omit<T, 'created' | 'released' | 'archived'> & {
  created?: string | null;
  released?: string | null;
  archived?: string | null;
};

type VideoCategoryFormRawValue = FormValueOf<IVideoCategory>;

type NewVideoCategoryFormRawValue = FormValueOf<NewVideoCategory>;

type VideoCategoryFormDefaults = Pick<NewVideoCategory, 'id' | 'created' | 'released' | 'archived'>;

type VideoCategoryFormGroupContent = {
  id: FormControl<VideoCategoryFormRawValue['id'] | NewVideoCategory['id']>;
  code: FormControl<VideoCategoryFormRawValue['code']>;
  title: FormControl<VideoCategoryFormRawValue['title']>;
  label: FormControl<VideoCategoryFormRawValue['label']>;
  created: FormControl<VideoCategoryFormRawValue['created']>;
  released: FormControl<VideoCategoryFormRawValue['released']>;
  archived: FormControl<VideoCategoryFormRawValue['archived']>;
};

export type VideoCategoryFormGroup = FormGroup<VideoCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VideoCategoryFormService {
  createVideoCategoryFormGroup(videoCategory: VideoCategoryFormGroupInput = { id: null }): VideoCategoryFormGroup {
    const videoCategoryRawValue = this.convertVideoCategoryToVideoCategoryRawValue({
      ...this.getFormDefaults(),
      ...videoCategory,
    });
    return new FormGroup<VideoCategoryFormGroupContent>({
      id: new FormControl(
        { value: videoCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(videoCategoryRawValue.code, {
        validators: [Validators.required],
      }),
      title: new FormControl(videoCategoryRawValue.title, {
        validators: [Validators.required],
      }),
      label: new FormControl(videoCategoryRawValue.label, {
        validators: [Validators.required],
      }),
      created: new FormControl(videoCategoryRawValue.created),
      released: new FormControl(videoCategoryRawValue.released),
      archived: new FormControl(videoCategoryRawValue.archived),
    });
  }

  getVideoCategory(form: VideoCategoryFormGroup): IVideoCategory | NewVideoCategory {
    return this.convertVideoCategoryRawValueToVideoCategory(form.getRawValue() as VideoCategoryFormRawValue | NewVideoCategoryFormRawValue);
  }

  resetForm(form: VideoCategoryFormGroup, videoCategory: VideoCategoryFormGroupInput): void {
    const videoCategoryRawValue = this.convertVideoCategoryToVideoCategoryRawValue({ ...this.getFormDefaults(), ...videoCategory });
    form.reset(
      {
        ...videoCategoryRawValue,
        id: { value: videoCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VideoCategoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      released: currentTime,
      archived: currentTime,
    };
  }

  private convertVideoCategoryRawValueToVideoCategory(
    rawVideoCategory: VideoCategoryFormRawValue | NewVideoCategoryFormRawValue,
  ): IVideoCategory | NewVideoCategory {
    return {
      ...rawVideoCategory,
      created: dayjs(rawVideoCategory.created, DATE_TIME_FORMAT),
      released: dayjs(rawVideoCategory.released, DATE_TIME_FORMAT),
      archived: dayjs(rawVideoCategory.archived, DATE_TIME_FORMAT),
    };
  }

  private convertVideoCategoryToVideoCategoryRawValue(
    videoCategory: IVideoCategory | (Partial<NewVideoCategory> & VideoCategoryFormDefaults),
  ): VideoCategoryFormRawValue | PartialWithRequiredKeyOf<NewVideoCategoryFormRawValue> {
    return {
      ...videoCategory,
      created: videoCategory.created ? videoCategory.created.format(DATE_TIME_FORMAT) : undefined,
      released: videoCategory.released ? videoCategory.released.format(DATE_TIME_FORMAT) : undefined,
      archived: videoCategory.archived ? videoCategory.archived.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
