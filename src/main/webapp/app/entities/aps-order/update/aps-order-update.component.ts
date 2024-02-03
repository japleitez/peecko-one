import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsPlan } from 'app/entities/aps-plan/aps-plan.model';
import { ApsPlanService } from 'app/entities/aps-plan/service/aps-plan.service';
import { APS_ORDER_USER_ACCESS, ApsOrderAccess, IApsOrder } from '../aps-order.model';
import { ApsOrderService } from '../service/aps-order.service';
import { ApsOrderFormService, ApsOrderFormGroup } from './aps-order-form.service';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-aps-order-update',
  templateUrl: './aps-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf]
})
export class ApsOrderUpdateComponent implements OnInit {
  ua: ApsOrderAccess = this.getApsOrderAccess();
  isSaving = false;
  apsOrder: IApsOrder | null = null;

  apsPlansSharedCollection: IApsPlan[] = [];

  editForm: ApsOrderFormGroup = this.apsOrderFormService.createApsOrderFormGroup();

  constructor(
    protected apsOrderService: ApsOrderService,
    protected apsOrderFormService: ApsOrderFormService,
    protected apsPlanService: ApsPlanService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsPlan = (o1: IApsPlan | null, o2: IApsPlan | null): boolean => this.apsPlanService.compareApsPlan(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsOrder }) => {
      this.apsOrder = apsOrder;
      if (apsOrder) {
        this.updateForm(apsOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsOrder = this.apsOrderFormService.getApsOrder(this.editForm);
    if (apsOrder.id !== null) {
      this.subscribeToSaveResponse(this.apsOrderService.update(apsOrder));
    } else {
      this.subscribeToSaveResponse(this.apsOrderService.create(apsOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsOrder>>): void {
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

  protected updateForm(apsOrder: IApsOrder): void {
    this.apsOrder = apsOrder;
    this.apsOrderFormService.resetForm(this.editForm, apsOrder);

    this.apsPlansSharedCollection = this.apsPlanService.addApsPlanToCollectionIfMissing<IApsPlan>(
      this.apsPlansSharedCollection,
      apsOrder.apsPlan,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apsPlanService
      .query()
      .pipe(map((res: HttpResponse<IApsPlan[]>) => res.body ?? []))
      .pipe(map((apsPlans: IApsPlan[]) => this.apsPlanService.addApsPlanToCollectionIfMissing<IApsPlan>(apsPlans, this.apsOrder?.apsPlan)))
      .subscribe((apsPlans: IApsPlan[]) => (this.apsPlansSharedCollection = apsPlans));
  }

  protected getApsOrderAccess(): ApsOrderAccess {
    return APS_ORDER_USER_ACCESS;
  }

}
