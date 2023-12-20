import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsPlan } from '../aps-plan.model';
import { ApsPlanService } from '../service/aps-plan.service';

@Component({
  standalone: true,
  templateUrl: './aps-plan-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsPlanDeleteDialogComponent {
  apsPlan?: IApsPlan;

  constructor(
    protected apsPlanService: ApsPlanService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsPlanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
