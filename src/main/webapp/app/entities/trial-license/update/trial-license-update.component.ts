import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TRIAL_LICENSE_ACCESS, TrialLicenseAccess, ITrialLicense } from '../trial-license.model';
import { TrialLicenseService } from '../service/trial-license.service';
import { TrialLicenseFormService, TrialLicenseFormGroup } from './trial-license-form.service';

@Component({
  standalone: true,
  selector: 'jhi-trial-license-update',
  templateUrl: './trial-license-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrialLicenseUpdateComponent implements OnInit {
  ua: TrialLicenseAccess = this.getTrialLicenseAccess();
  isSaving = false;
  trialLicense: ITrialLicense | null = null;

  editForm: TrialLicenseFormGroup = this.trialLicenseFormService.createTrialLicenseFormGroup(undefined, this.getTrialLicenseAccess());

  constructor(
    protected trialLicenseService: TrialLicenseService,
    protected trialLicenseFormService: TrialLicenseFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trialLicense }) => {
      this.trialLicense = trialLicense;
      if (trialLicense) {
        this.updateForm(trialLicense);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trialLicense = this.trialLicenseFormService.getTrialLicense(this.editForm);
    if (this.trialLicense) {
      this.subscribeToSaveResponse(this.trialLicenseService.update(trialLicense as ITrialLicense));
    } else {
      this.subscribeToSaveResponse(this.trialLicenseService.create(trialLicense as ITrialLicense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrialLicense>>): void {
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

  protected updateForm(trialLicense: ITrialLicense): void {
    this.trialLicense = trialLicense;
    this.trialLicenseFormService.resetForm(this.editForm, trialLicense);
  }

  protected getTrialLicenseAccess(): TrialLicenseAccess {
    return TRIAL_LICENSE_ACCESS;
  }
}
