import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVideoItem, NewVideoItem } from '../video-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVideoItem for edit and NewVideoItemFormGroupInput for create.
 */
type VideoItemFormGroupInput = IVideoItem | PartialWithRequiredKeyOf<NewVideoItem>;

type VideoItemFormDefaults = Pick<NewVideoItem, 'id'>;

type VideoItemFormGroupContent = {
  id: FormControl<IVideoItem['id'] | NewVideoItem['id']>;
  previous: FormControl<IVideoItem['previous']>;
  code: FormControl<IVideoItem['code']>;
  next: FormControl<IVideoItem['next']>;
  playList: FormControl<IVideoItem['playList']>;
};

export type VideoItemFormGroup = FormGroup<VideoItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VideoItemFormService {
  createVideoItemFormGroup(videoItem: VideoItemFormGroupInput = { id: null }): VideoItemFormGroup {
    const videoItemRawValue = {
      ...this.getFormDefaults(),
      ...videoItem,
    };
    return new FormGroup<VideoItemFormGroupContent>({
      id: new FormControl(
        { value: videoItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      previous: new FormControl(videoItemRawValue.previous),
      code: new FormControl(videoItemRawValue.code),
      next: new FormControl(videoItemRawValue.next),
      playList: new FormControl(videoItemRawValue.playList),
    });
  }

  getVideoItem(form: VideoItemFormGroup): IVideoItem | NewVideoItem {
    return form.getRawValue() as IVideoItem | NewVideoItem;
  }

  resetForm(form: VideoItemFormGroup, videoItem: VideoItemFormGroupInput): void {
    const videoItemRawValue = { ...this.getFormDefaults(), ...videoItem };
    form.reset(
      {
        ...videoItemRawValue,
        id: { value: videoItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VideoItemFormDefaults {
    return {
      id: null,
    };
  }
}
