import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { COACH_ACCESS, CoachAccess, ICoach } from '../coach.model';
import { ARTICLE_ACCESS } from '../../article/article.model';

@Component({
  standalone: true,
  selector: 'jhi-coach-detail',
  templateUrl: './coach-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CoachDetailComponent {
  ua: CoachAccess = COACH_ACCESS;
  @Input() coach: ICoach | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
