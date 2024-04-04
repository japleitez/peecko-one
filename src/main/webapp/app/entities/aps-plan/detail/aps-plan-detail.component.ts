import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_PLAN_ACCESS, ApsPlanAccess, IApsPlan } from '../aps-plan.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-aps-plan-detail',
  templateUrl: './aps-plan-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ApsPlanDetailComponent {
  ua: ApsPlanAccess = APS_PLAN_ACCESS;
  @Input() apsPlan: IApsPlan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
