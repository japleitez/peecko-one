import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { ApsOrderService } from 'app/entities/aps-order/service/aps-order.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';

@Component({
  standalone: true,
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;

  apsOrdersSharedCollection: IApsOrder[] = [];

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected apsOrderService: ApsOrderService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsOrder = (o1: IApsOrder | null, o2: IApsOrder | null): boolean => this.apsOrderService.compareApsOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.apsOrdersSharedCollection = this.apsOrderService.addApsOrderToCollectionIfMissing<IApsOrder>(
      this.apsOrdersSharedCollection,
      invoice.apsOrder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apsOrderService
      .query()
      .pipe(map((res: HttpResponse<IApsOrder[]>) => res.body ?? []))
      .pipe(
        map((apsOrders: IApsOrder[]) =>
          this.apsOrderService.addApsOrderToCollectionIfMissing<IApsOrder>(apsOrders, this.invoice?.apsOrder),
        ),
      )
      .subscribe((apsOrders: IApsOrder[]) => (this.apsOrdersSharedCollection = apsOrders));
  }
}
