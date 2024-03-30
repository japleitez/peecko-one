import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInvoiceItem, INVOICE_ITEM_ACCESS, InvoiceItemAccess } from '../invoice-item.model';
import { INVOICE_ACCESS } from '../../invoice/invoice.model';

@Component({
  standalone: true,
  selector: 'jhi-invoice-item-detail',
  templateUrl: './invoice-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InvoiceItemDetailComponent {
  ua: InvoiceItemAccess = INVOICE_ITEM_ACCESS;
  @Input() invoiceItem: IInvoiceItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
