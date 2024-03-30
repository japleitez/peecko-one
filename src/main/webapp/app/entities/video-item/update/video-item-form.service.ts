import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVideoItem, NewVideoItem, VIDEO_ITEM_ACCESS, VideoItemAccess } from '../video-item.model';

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
  createVideoItemFormGroup(videoItem: VideoItemFormGroupInput = { id: null }, ua: VideoItemAccess = VIDEO_ITEM_ACCESS): VideoItemFormGroup {
    const videoItemRawValue = {
      ...this.getFormDefaults(),
      ...videoItem,
    };
    return new FormGroup<VideoItemFormGroupContent>({
      id: new FormControl(
        { value: videoItemRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl({ value: videoItemRawValue.code, disabled: ua.code.disabled }),
      previous: new FormControl({ value: videoItemRawValue.previous, disabled: ua.previous.disabled }),
      next: new FormControl({ value: videoItemRawValue.next, disabled: ua.next.disabled }),
      playList: new FormControl({ value: videoItemRawValue.playList, disabled: ua.playList.disabled }),
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
