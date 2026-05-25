import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe } from 'app/shared/date';
import { TRIAL_LICENSE_ACCESS, TrialLicenseAccess, ITrialLicense } from '../trial-license.model';
import { CUSTOMER_USER_ACCESS } from '../../customer/customer.model';

@Component({
  standalone: true,
  selector: 'jhi-trial-license-detail',
  templateUrl: './trial-license-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatePipe],
})
export class TrialLicenseDetailComponent {
  ua: TrialLicenseAccess = TRIAL_LICENSE_ACCESS;
  @Input() trialLicense: ITrialLicense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
