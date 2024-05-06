import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { CustomerState } from 'app/entities/enumerations/customer-state.model';
import { CustomerService } from '../service/customer.service';
import { CUSTOMER_USER_ACCESS, CustomerAccess, ICustomer } from '../customer.model';
import { CustomerFormService, CustomerFormGroup } from './customer-form.service';
import { AgencySelectComponent } from '../../agency/agency-select/agency-select.component';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, AgencySelectComponent, NgIf]
})
export class CustomerUpdateComponent implements OnInit {
  ua: CustomerAccess = this.getCustomerUserAccess();
  isSaving = false;
  customer: ICustomer | null = null;
  customerStateValues = Object.keys(CustomerState);

  agenciesSharedCollection: IAgency[] = [];

  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup(undefined, this.getCustomerUserAccess());

  constructor(
    protected customerService: CustomerService,
    protected customerFormService: CustomerFormService,
    protected agencyService: AgencyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAgency = (o1: IAgency | null, o2: IAgency | null): boolean => this.agencyService.compareAgency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id !== null) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);

    this.agenciesSharedCollection = this.agencyService.addAgencyToCollectionIfMissing<IAgency>(
      this.agenciesSharedCollection,
      customer.agency,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.agencyService
      .query()
      .pipe(map((res: HttpResponse<IAgency[]>) => res.body ?? []))
      .pipe(map((agencies: IAgency[]) => this.agencyService.addAgencyToCollectionIfMissing<IAgency>(agencies, this.customer?.agency)))
      .subscribe((agencies: IAgency[]) => (this.agenciesSharedCollection = agencies));
  }

  protected getCustomerUserAccess(): CustomerAccess {
    return CUSTOMER_USER_ACCESS
  }

  agencyControl() {
    return this.editForm.get('agency') as FormControl<IAgency | string | null>;
  }

}
