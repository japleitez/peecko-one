import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { APS_DEVICE_ACCESS, ApsDeviceAccess, IApsDevice } from '../aps-device.model';
import { AGENCY_USER_ACCESS } from '../../agency/agency.model';

@Component({
  standalone: true,
  selector: 'jhi-aps-device-detail',
  templateUrl: './aps-device-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ApsDeviceDetailComponent {
  ua: ApsDeviceAccess = APS_DEVICE_ACCESS;
  @Input() apsDevice: IApsDevice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
