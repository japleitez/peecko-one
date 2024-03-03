import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { ApsUserService } from 'app/entities/aps-user/service/aps-user.service';
import { APS_DEVICE_ACCESS, ApsDeviceAccess, IApsDevice } from '../aps-device.model';
import { ApsDeviceService } from '../service/aps-device.service';
import { ApsDeviceFormService, ApsDeviceFormGroup } from './aps-device-form.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-device-update',
  templateUrl: './aps-device-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApsDeviceUpdateComponent implements OnInit {
  ua: ApsDeviceAccess = this.getApsDeviceAccess();
  isSaving = false;
  apsDevice: IApsDevice | null = null;

  apsUsersSharedCollection: IApsUser[] = [];

  editForm: ApsDeviceFormGroup = this.apsDeviceFormService.createApsDeviceFormGroup();

  constructor(
    protected apsDeviceService: ApsDeviceService,
    protected apsDeviceFormService: ApsDeviceFormService,
    protected apsUserService: ApsUserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsUser = (o1: IApsUser | null, o2: IApsUser | null): boolean => this.apsUserService.compareApsUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsDevice }) => {
      this.apsDevice = apsDevice;
      if (apsDevice) {
        this.updateForm(apsDevice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsDevice = this.apsDeviceFormService.getApsDevice(this.editForm);
    if (apsDevice.id !== null) {
      this.subscribeToSaveResponse(this.apsDeviceService.update(apsDevice));
    } else {
      this.subscribeToSaveResponse(this.apsDeviceService.create(apsDevice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsDevice>>): void {
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

  protected updateForm(apsDevice: IApsDevice): void {
    this.apsDevice = apsDevice;
    this.apsDeviceFormService.resetForm(this.editForm, apsDevice);

    this.apsUsersSharedCollection = this.apsUserService.addApsUserToCollectionIfMissing<IApsUser>(
      this.apsUsersSharedCollection,
      apsDevice.apsUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apsUserService
      .query()
      .pipe(map((res: HttpResponse<IApsUser[]>) => res.body ?? []))
      .pipe(map((apsUsers: IApsUser[]) => this.apsUserService.addApsUserToCollectionIfMissing<IApsUser>(apsUsers, this.apsDevice?.apsUser)))
      .subscribe((apsUsers: IApsUser[]) => (this.apsUsersSharedCollection = apsUsers));
  }

  protected getApsDeviceAccess(): ApsDeviceAccess {
    return APS_DEVICE_ACCESS;
  }

}
