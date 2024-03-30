import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { ProductType } from 'app/entities/enumerations/product-type.model';
import { InvoiceItemService } from '../service/invoice-item.service';
import { IInvoiceItem, INVOICE_ITEM_ACCESS, InvoiceItemAccess } from '../invoice-item.model';
import { InvoiceItemFormService, InvoiceItemFormGroup } from './invoice-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-invoice-item-update',
  templateUrl: './invoice-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceItemUpdateComponent implements OnInit {
  ua: InvoiceItemAccess = this.getInvoiceItemAccess();
  isSaving = false;
  invoiceItem: IInvoiceItem | null = null;
  productTypeValues = Object.keys(ProductType);

  invoicesSharedCollection: IInvoice[] = [];

  editForm: InvoiceItemFormGroup = this.invoiceItemFormService.createInvoiceItemFormGroup(undefined, this.getInvoiceItemAccess());

  constructor(
    protected invoiceItemService: InvoiceItemService,
    protected invoiceItemFormService: InvoiceItemFormService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItem }) => {
      this.invoiceItem = invoiceItem;
      if (invoiceItem) {
        this.updateForm(invoiceItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceItem = this.invoiceItemFormService.getInvoiceItem(this.editForm);
    if (invoiceItem.id !== null) {
      this.subscribeToSaveResponse(this.invoiceItemService.update(invoiceItem));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemService.create(invoiceItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceItem>>): void {
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

  protected updateForm(invoiceItem: IInvoiceItem): void {
    this.invoiceItem = invoiceItem;
    this.invoiceItemFormService.resetForm(this.editForm, invoiceItem);

    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
      this.invoicesSharedCollection,
      invoiceItem.invoice,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceItem?.invoice)),
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }

  protected getInvoiceItemAccess(): InvoiceItemAccess {
    return INVOICE_ITEM_ACCESS;
  }

}
