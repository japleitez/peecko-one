import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_PRICING_ACCESS, ApsPricingAccess, IApsPricing } from '../aps-pricing.model';
import { APS_PLAN_USER_ACCESS } from '../../aps-plan/aps-plan.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-aps-pricing-detail',
  templateUrl: './aps-pricing-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ApsPricingDetailComponent {
  ua: ApsPricingAccess = APS_PRICING_ACCESS;
  @Input() apsPricing: IApsPricing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
