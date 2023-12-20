import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IArticleCategory } from '../article-category.model';
import { ArticleCategoryService } from '../service/article-category.service';

@Component({
  standalone: true,
  templateUrl: './article-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ArticleCategoryDeleteDialogComponent {
  articleCategory?: IArticleCategory;

  constructor(
    protected articleCategoryService: ArticleCategoryService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.articleCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
