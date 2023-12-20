import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlayList, NewPlayList } from '../play-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayList for edit and NewPlayListFormGroupInput for create.
 */
type PlayListFormGroupInput = IPlayList | PartialWithRequiredKeyOf<NewPlayList>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlayList | NewPlayList> = Omit<T, 'created' | 'updated'> & {
  created?: string | null;
  updated?: string | null;
};

type PlayListFormRawValue = FormValueOf<IPlayList>;

type NewPlayListFormRawValue = FormValueOf<NewPlayList>;

type PlayListFormDefaults = Pick<NewPlayList, 'id' | 'created' | 'updated'>;

type PlayListFormGroupContent = {
  id: FormControl<PlayListFormRawValue['id'] | NewPlayList['id']>;
  name: FormControl<PlayListFormRawValue['name']>;
  counter: FormControl<PlayListFormRawValue['counter']>;
  created: FormControl<PlayListFormRawValue['created']>;
  updated: FormControl<PlayListFormRawValue['updated']>;
  apsUser: FormControl<PlayListFormRawValue['apsUser']>;
};

export type PlayListFormGroup = FormGroup<PlayListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayListFormService {
  createPlayListFormGroup(playList: PlayListFormGroupInput = { id: null }): PlayListFormGroup {
    const playListRawValue = this.convertPlayListToPlayListRawValue({
      ...this.getFormDefaults(),
      ...playList,
    });
    return new FormGroup<PlayListFormGroupContent>({
      id: new FormControl(
        { value: playListRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(playListRawValue.name, {
        validators: [Validators.required],
      }),
      counter: new FormControl(playListRawValue.counter, {
        validators: [Validators.required],
      }),
      created: new FormControl(playListRawValue.created, {
        validators: [Validators.required],
      }),
      updated: new FormControl(playListRawValue.updated, {
        validators: [Validators.required],
      }),
      apsUser: new FormControl(playListRawValue.apsUser),
    });
  }

  getPlayList(form: PlayListFormGroup): IPlayList | NewPlayList {
    return this.convertPlayListRawValueToPlayList(form.getRawValue() as PlayListFormRawValue | NewPlayListFormRawValue);
  }

  resetForm(form: PlayListFormGroup, playList: PlayListFormGroupInput): void {
    const playListRawValue = this.convertPlayListToPlayListRawValue({ ...this.getFormDefaults(), ...playList });
    form.reset(
      {
        ...playListRawValue,
        id: { value: playListRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlayListFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      created: currentTime,
      updated: currentTime,
    };
  }

  private convertPlayListRawValueToPlayList(rawPlayList: PlayListFormRawValue | NewPlayListFormRawValue): IPlayList | NewPlayList {
    return {
      ...rawPlayList,
      created: dayjs(rawPlayList.created, DATE_TIME_FORMAT),
      updated: dayjs(rawPlayList.updated, DATE_TIME_FORMAT),
    };
  }

  private convertPlayListToPlayListRawValue(
    playList: IPlayList | (Partial<NewPlayList> & PlayListFormDefaults),
  ): PlayListFormRawValue | PartialWithRequiredKeyOf<NewPlayListFormRawValue> {
    return {
      ...playList,
      created: playList.created ? playList.created.format(DATE_TIME_FORMAT) : undefined,
      updated: playList.updated ? playList.updated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
