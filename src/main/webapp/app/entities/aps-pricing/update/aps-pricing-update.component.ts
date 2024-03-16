import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { IApsPricing } from '../aps-pricing.model';
import { ApsPricingService } from '../service/aps-pricing.service';
import { ApsPricingFormService, ApsPricingFormGroup } from './aps-pricing-form.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-pricing-update',
  templateUrl: './aps-pricing-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApsPricingUpdateComponent implements OnInit {
  isSaving = false;
  apsPricing: IApsPricing | null = null;

  editForm: ApsPricingFormGroup = this.apsPricingFormService.createApsPricingFormGroup();

  constructor(
    protected apsPricingService: ApsPricingService,
    protected apsPricingFormService: ApsPricingFormService,
    protected agencyService: AgencyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAgency = (o1: IAgency | null, o2: IAgency | null): boolean => this.agencyService.compareAgency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsPricing }) => {
      this.apsPricing = apsPricing;
      if (apsPricing) {
        this.updateForm(apsPricing);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsPricing = this.apsPricingFormService.getApsPricing(this.editForm);
    if (apsPricing.id !== null) {
      this.subscribeToSaveResponse(this.apsPricingService.update(apsPricing));
    } else {
      this.subscribeToSaveResponse(this.apsPricingService.create(apsPricing));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsPricing>>): void {
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

  protected updateForm(apsPricing: IApsPricing): void {
    this.apsPricing = apsPricing;
    this.apsPricingFormService.resetForm(this.editForm, apsPricing);
  }

}
