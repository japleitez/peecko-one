import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Language } from 'app/entities/enumerations/language.model';
import { INotification, NOTIFICATION_ACCESS, NotificationAccess } from '../notification.model';
import { NotificationService } from '../service/notification.service';
import { NotificationFormService, NotificationFormGroup } from './notification-form.service';
import { CustomerSelectorComponent } from '../../customer/customer-selector/customer-selector.component';
import { NgIf } from '@angular/common';
import { ICustomer } from '../../customer/customer.model';

@Component({
  standalone: true,
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, CustomerSelectorComponent, NgIf]
})
export class NotificationUpdateComponent implements OnInit {
  ua: NotificationAccess = this.getNotificationAccess();
  isSaving = false;
  notification: INotification | null = null;
  languageValues = Object.keys(Language);

  editForm: NotificationFormGroup = this.notificationFormService.createNotificationFormGroup(undefined, this.getNotificationAccess());

  constructor(
    protected notificationService: NotificationService,
    protected notificationFormService: NotificationFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      this.notification = notification;
      if (notification) {
        this.updateForm(notification);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.notificationFormService.getNotification(this.editForm);
    if (notification.id !== null) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(notification: INotification): void {
    this.notification = notification;
    this.notificationFormService.resetForm(this.editForm, notification);
  }

  protected getNotificationAccess(): NotificationAccess {
    return NOTIFICATION_ACCESS;
  }

  customerControl() {
    return this.editForm.get('customer') as FormControl<ICustomer | string | null>;
  }

}
