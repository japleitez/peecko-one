import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_MEMBERSHIP_USER_ACCESS, ApsMembershipAccess, IApsMembership } from '../aps-membership.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-aps-membership-detail',
  templateUrl: './aps-membership-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ApsMembershipDetailComponent {
  ua: ApsMembershipAccess = APS_MEMBERSHIP_USER_ACCESS;
  @Input() apsMembership: IApsMembership | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
