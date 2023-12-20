import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVideo } from '../video.model';
import { VideoService } from '../service/video.service';

@Component({
  standalone: true,
  templateUrl: './video-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VideoDeleteDialogComponent {
  video?: IVideo;

  constructor(
    protected videoService: VideoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.videoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
