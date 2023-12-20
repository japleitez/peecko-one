import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsUser } from '../aps-user.model';
import { ApsUserService } from '../service/aps-user.service';

@Component({
  standalone: true,
  templateUrl: './aps-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsUserDeleteDialogComponent {
  apsUser?: IApsUser;

  constructor(
    protected apsUserService: ApsUserService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
