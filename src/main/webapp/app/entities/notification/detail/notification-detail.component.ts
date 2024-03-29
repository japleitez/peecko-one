import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { INotification, NOTIFICATION_ACCESS, NotificationAccess } from '../notification.model';
import { ARTICLE_ACCESS } from '../../article/article.model';

@Component({
  standalone: true,
  selector: 'jhi-notification-detail',
  templateUrl: './notification-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class NotificationDetailComponent {
  ua: NotificationAccess = NOTIFICATION_ACCESS;
  @Input() notification: INotification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
