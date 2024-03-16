import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { AGENCY_USER_ACCESS, AgencyAccess, IAgency } from '../agency.model';

@Component({
  standalone: true,
  selector: 'jhi-agency-detail',
  templateUrl: './agency-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AgencyDetailComponent {
  ua: AgencyAccess = AGENCY_USER_ACCESS;
  @Input() agency: IAgency | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
