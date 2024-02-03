import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_ORDER_USER_ACCESS, ApsOrderAccess, IApsOrder } from '../aps-order.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-aps-order-detail',
  templateUrl: './aps-order-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ApsOrderDetailComponent {
  ua: ApsOrderAccess = APS_ORDER_USER_ACCESS;
  @Input() apsOrder: IApsOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
