import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IVideoCategory, VIDEO_CATEGORY_ACCESS, VideoCategoryAccess } from '../video-category.model';
import { VIDEO_ACCESS } from '../../video/video.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-video-category-detail',
  templateUrl: './video-category-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class VideoCategoryDetailComponent {
  ua: VideoCategoryAccess = VIDEO_CATEGORY_ACCESS;
  @Input() videoCategory: IVideoCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
