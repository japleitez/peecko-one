import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVideoCategory } from '../video-category.model';
import { VideoCategoryService } from '../service/video-category.service';

@Component({
  standalone: true,
  templateUrl: './video-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VideoCategoryDeleteDialogComponent {
  videoCategory?: IVideoCategory;

  constructor(
    protected videoCategoryService: VideoCategoryService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.videoCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
