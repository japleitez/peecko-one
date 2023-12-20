import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsMembership } from '../aps-membership.model';
import { ApsMembershipService } from '../service/aps-membership.service';

@Component({
  standalone: true,
  templateUrl: './aps-membership-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsMembershipDeleteDialogComponent {
  apsMembership?: IApsMembership;

  constructor(
    protected apsMembershipService: ApsMembershipService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsMembershipService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
