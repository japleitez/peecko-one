import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILabelTranslation, LABEL_ACCESS, LabelAccess } from '../label-translation.model';
import { ARTICLE_ACCESS } from '../../article/article.model';

@Component({
  standalone: true,
  selector: 'jhi-label-translation-detail',
  templateUrl: './label-translation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LabelTranslationDetailComponent {
  ua: LabelAccess = LABEL_ACCESS;
  @Input() labelTranslation: ILabelTranslation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
