import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInvoice, INVOICE_ACCESS, InvoiceAccess } from '../invoice.model';
import { COACH_ACCESS } from '../../coach/coach.model';

@Component({
  standalone: true,
  selector: 'jhi-invoice-detail',
  templateUrl: './invoice-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InvoiceDetailComponent {
  ua: InvoiceAccess = INVOICE_ACCESS;
  @Input() invoice: IInvoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
