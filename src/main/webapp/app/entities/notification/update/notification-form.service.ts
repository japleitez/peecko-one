import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INotification, NewNotification, NOTIFICATION_ACCESS, NotificationAccess } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id'>;

type NotificationFormGroupContent = {
  id: FormControl<INotification['id'] | NewNotification['id']>;
  companyId: FormControl<INotification['companyId']>;
  title: FormControl<INotification['title']>;
  message: FormControl<INotification['message']>;
  language: FormControl<INotification['language']>;
  imageUrl: FormControl<INotification['imageUrl']>;
  videoUrl: FormControl<INotification['videoUrl']>;
  starts: FormControl<INotification['starts']>;
  expires: FormControl<INotification['expires']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationFormService {
  createNotificationFormGroup(
    notification: NotificationFormGroupInput = { id: null },
    ua: NotificationAccess = NOTIFICATION_ACCESS,
  ): NotificationFormGroup {
    const notificationRawValue = {
      ...this.getFormDefaults(),
      ...notification,
    };
    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: ua.id.disabled },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      companyId: new FormControl(
        { value: notificationRawValue.companyId, disabled: ua.companyId.disabled },
        { validators: [Validators.required] },
      ),
      title: new FormControl({ value: notificationRawValue.title, disabled: ua.title.disabled }, { validators: [Validators.required] }),
      message: new FormControl(
        { value: notificationRawValue.message, disabled: ua.message.disabled },
        { validators: [Validators.required] },
      ),
      language: new FormControl(
        { value: notificationRawValue.language, disabled: ua.language.disabled },
        { validators: [Validators.required] },
      ),
      imageUrl: new FormControl({ value: notificationRawValue.imageUrl, disabled: ua.imageUrl.disabled }),
      videoUrl: new FormControl({ value: notificationRawValue.videoUrl, disabled: ua.videoUrl.disabled }),
      starts: new FormControl({ value: notificationRawValue.starts, disabled: ua.starts.disabled }),
      expires: new FormControl({ value: notificationRawValue.expires, disabled: ua.expires.disabled }),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return form.getRawValue() as INotification | NewNotification;
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = { ...this.getFormDefaults(), ...notification };
    form.reset(
      {
        ...notificationRawValue,
        id: { value: notificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationFormDefaults {
    return {
      id: null,
    };
  }
}
