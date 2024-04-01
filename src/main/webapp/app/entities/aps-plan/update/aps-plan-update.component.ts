import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { PricingType } from 'app/entities/enumerations/pricing-type.model';
import { PlanState } from 'app/entities/enumerations/plan-state.model';
import { ApsPlanService } from '../service/aps-plan.service';
import { APS_PLAN_USER_ACCESS, ApsPlanAccess, IApsPlan } from '../aps-plan.model';
import { ApsPlanFormService, ApsPlanFormGroup } from './aps-plan-form.service';
import { NgIf } from '@angular/common';
import { CustomerSelectorComponent } from '../../customer/customer-selector/customer-selector.component';

@Component({
  standalone: true,
  selector: 'jhi-aps-plan-update',
  templateUrl: './aps-plan-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf, CustomerSelectorComponent]
})
export class ApsPlanUpdateComponent implements OnInit {
  ua: ApsPlanAccess = this.getApsPlanAccess();
  isSaving = false;
  apsPlan: IApsPlan | null = null;
  pricingTypeValues = Object.keys(PricingType);
  planStateValues = Object.keys(PlanState);

  customersSharedCollection: ICustomer[] = [];

  editForm: ApsPlanFormGroup = this.apsPlanFormService.createApsPlanFormGroup();

  constructor(
    protected apsPlanService: ApsPlanService,
    protected apsPlanFormService: ApsPlanFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsPlan }) => {
      this.apsPlan = apsPlan;
      if (apsPlan) {
        this.updateForm(apsPlan);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsPlan = this.apsPlanFormService.getApsPlan(this.editForm);
    if (apsPlan.id !== null) {
      this.subscribeToSaveResponse(this.apsPlanService.update(apsPlan));
    } else {
      this.subscribeToSaveResponse(this.apsPlanService.create(apsPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsPlan>>): void {
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

  protected updateForm(apsPlan: IApsPlan): void {
    this.apsPlan = apsPlan;
    this.apsPlanFormService.resetForm(this.editForm, apsPlan);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      apsPlan.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.apsPlan?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }

  protected getApsPlanAccess(): ApsPlanAccess {
    return APS_PLAN_USER_ACCESS;
  }
  fc(name: string) {
    return this.editForm.get(name) as FormControl<ICustomer | string | null>;
  }

}
