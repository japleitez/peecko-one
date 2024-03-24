import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IVideo, VIDEO_ACCESS, VideoAccess } from '../video.model';
import { APS_PRICING_ACCESS } from '../../aps-pricing/aps-pricing.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-video-detail',
  templateUrl: './video-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class VideoDetailComponent {
  ua: VideoAccess = VIDEO_ACCESS;
  @Input() video: IVideo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
