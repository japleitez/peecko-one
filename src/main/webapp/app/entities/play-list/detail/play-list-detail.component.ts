import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPlayList, PLAYLIST_ACCESS, PlayListAccess } from '../play-list.model';
import { NOTIFICATION_ACCESS } from '../../notification/notification.model';

@Component({
  standalone: true,
  selector: 'jhi-play-list-detail',
  templateUrl: './play-list-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlayListDetailComponent {
  ua: PlayListAccess = PLAYLIST_ACCESS;
  @Input() playList: IPlayList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
