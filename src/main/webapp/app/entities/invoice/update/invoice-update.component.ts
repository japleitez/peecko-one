import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { ApsOrderService } from 'app/entities/aps-order/service/aps-order.service';
import { ProductType } from 'app/entities/enumerations/product-type.model';
import { IInvoiceItem, NewInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { IInvoice, INVOICE_ACCESS, InvoiceAccess } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';

@Component({
  standalone: true,
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceUpdateComponent implements OnInit {
  ua: InvoiceAccess = this.getInvoiceAccess();
  isSaving = false;
  invoice: IInvoice | null = null;
  customerName: string | null = null;
  planContract: string | null = null;
  invoiceItems: IInvoiceItem[] = [];

  productTypes = Object.keys(ProductType) as Array<keyof typeof ProductType>;
  newItem = { type: 'APP' as keyof typeof ProductType, description: '', quantity: 1, unitPrice: 0 };
  isAddingItem = false;

  apsOrdersSharedCollection: IApsOrder[] = [];

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup(undefined, this.getInvoiceAccess());

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected invoiceItemService: InvoiceItemService,
    protected apsOrderService: ApsOrderService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareApsOrder = (o1: IApsOrder | null, o2: IApsOrder | null): boolean => this.apsOrderService.compareApsOrder(o1, o2);

  get itemSubtotal(): number {
    return Math.round(this.newItem.quantity * this.newItem.unitPrice * 100) / 100;
  }

  ngOnInit(): void {
    const state = history.state;
    this.customerName = state?.customerName ?? null;
    this.planContract = state?.planContract ?? null;

    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.partialUpdate(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  deleteItem(item: IInvoiceItem): void {
    this.invoiceItemService.delete(item.id).subscribe({
      next: () => {
        this.invoiceItems = this.invoiceItems.filter(i => i.id !== item.id);
        this.recalculateTotals();
      },
    });
  }

  addItem(): void {
    if (!this.invoice?.id) return;
    const item: NewInvoiceItem = {
      id: null,
      type: this.newItem.type,
      description: this.newItem.description,
      quantity: this.newItem.quantity,
      unitPrice: this.newItem.unitPrice,
      subtotal: this.itemSubtotal,
      invoice: { id: this.invoice.id } as IInvoice,
    };
    this.invoiceItemService.create(item).subscribe({
      next: res => {
        if (res.body) {
          this.invoiceItems = [...this.invoiceItems, res.body];
          this.recalculateTotals();
          this.newItem = { type: 'APP', description: '', quantity: 1, unitPrice: 0 };
          this.isAddingItem = false;
        }
      },
    });
  }

  recalculateTotals(): void {
    const subtotal = Math.round(this.invoiceItems.reduce((sum, item) => sum + (item.subtotal ?? 0), 0) * 100) / 100;
    const vatRate = this.editForm.get('vatRate')?.value ?? 0;
    const vat = Math.round(subtotal * (vatRate / 100) * 100) / 100;
    const total = Math.round((subtotal + vat) * 100) / 100;
    this.editForm.patchValue({ subtotal, vat, total });
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
    this.invoiceItems = invoice.invoiceItems ?? [];
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.apsOrdersSharedCollection = this.apsOrderService.addApsOrderToCollectionIfMissing<IApsOrder>(
      this.apsOrdersSharedCollection,
      invoice.apsOrder,
    );
  }

  protected getInvoiceAccess(): InvoiceAccess {
    return INVOICE_ACCESS;
  }
}
