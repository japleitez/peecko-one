import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IArticleSeries } from '../article-series.model';
import { ArticleSeriesService } from '../service/article-series.service';

@Component({
  standalone: true,
  templateUrl: './article-series-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ArticleSeriesDeleteDialogComponent {
  articleSeries?: IArticleSeries;

  constructor(
    protected articleSeriesService: ArticleSeriesService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.articleSeriesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
