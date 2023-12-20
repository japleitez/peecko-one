import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlayList } from '../play-list.model';
import { PlayListService } from '../service/play-list.service';

@Component({
  standalone: true,
  templateUrl: './play-list-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlayListDeleteDialogComponent {
  playList?: IPlayList;

  constructor(
    protected playListService: PlayListService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playListService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
