import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_USER_ACCESS, ApsUserAccess, IApsUser } from '../aps-user.model';
import { CUSTOMER_USER_ACCESS } from '../../customer/customer.model';

@Component({
  standalone: true,
  selector: 'jhi-aps-user-detail',
  templateUrl: './aps-user-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ApsUserDetailComponent {
  ua: ApsUserAccess = APS_USER_ACCESS;
  @Input() apsUser: IApsUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
