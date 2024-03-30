import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IVideoItem, VIDEO_ITEM_ACCESS, VideoItemAccess } from '../video-item.model';
import { ARTICLE_ACCESS } from '../../article/article.model';

@Component({
  standalone: true,
  selector: 'jhi-video-item-detail',
  templateUrl: './video-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VideoItemDetailComponent {
  ua: VideoItemAccess = VIDEO_ITEM_ACCESS;
  @Input() videoItem: IVideoItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
