import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICoach } from '../coach.model';
import { CoachService } from '../service/coach.service';

@Component({
  standalone: true,
  templateUrl: './coach-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CoachDeleteDialogComponent {
  coach?: ICoach;

  constructor(
    protected coachService: CoachService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coachService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
