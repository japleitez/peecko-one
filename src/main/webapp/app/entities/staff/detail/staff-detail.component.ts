import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IStaff, STAFF_ACCESS, StaffAccess } from '../staff.model';
import { PLAYLIST_ACCESS } from '../../play-list/play-list.model';

@Component({
  standalone: true,
  selector: 'jhi-staff-detail',
  templateUrl: './staff-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StaffDetailComponent {
  ua: StaffAccess = STAFF_ACCESS;
  @Input() staff: IStaff | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
