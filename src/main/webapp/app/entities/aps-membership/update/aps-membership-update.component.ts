import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { ApsOrderService } from 'app/entities/aps-order/service/aps-order.service';
import { IApsMembership } from '../aps-membership.model';
import { ApsMembershipService } from '../service/aps-membership.service';
import { ApsMembershipFormService, ApsMembershipFormGroup } from './aps-membership-form.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-membership-update',
  templateUrl: './aps-membership-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApsMembershipUpdateComponent implements OnInit {
  isSaving = false;
  apsMembership: IApsMembership | null = null;

  apsOrdersSharedCollection: IApsOrder[] = [];

  editForm: ApsMembershipFormGroup = this.apsMembershipFormService.createApsMembershipFormGroup();

  constructor(
    protected apsMembershipService: ApsMembershipService,
    protected apsMembershipFormService: ApsMembershipFormService,
    protected apsOrderService: ApsOrderService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsOrder = (o1: IApsOrder | null, o2: IApsOrder | null): boolean => this.apsOrderService.compareApsOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsMembership }) => {
      this.apsMembership = apsMembership;
      if (apsMembership) {
        this.updateForm(apsMembership);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsMembership = this.apsMembershipFormService.getApsMembership(this.editForm);
    if (apsMembership.id !== null) {
      this.subscribeToSaveResponse(this.apsMembershipService.update(apsMembership));
    } else {
      this.subscribeToSaveResponse(this.apsMembershipService.create(apsMembership));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsMembership>>): void {
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

  protected updateForm(apsMembership: IApsMembership): void {
    this.apsMembership = apsMembership;
    this.apsMembershipFormService.resetForm(this.editForm, apsMembership);

    this.apsOrdersSharedCollection = this.apsOrderService.addApsOrderToCollectionIfMissing<IApsOrder>(
      this.apsOrdersSharedCollection,
      apsMembership.apsOrder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apsOrderService
      .query()
      .pipe(map((res: HttpResponse<IApsOrder[]>) => res.body ?? []))
      .pipe(
        map((apsOrders: IApsOrder[]) =>
          this.apsOrderService.addApsOrderToCollectionIfMissing<IApsOrder>(apsOrders, this.apsMembership?.apsOrder),
        ),
      )
      .subscribe((apsOrders: IApsOrder[]) => (this.apsOrdersSharedCollection = apsOrders));
  }
}
