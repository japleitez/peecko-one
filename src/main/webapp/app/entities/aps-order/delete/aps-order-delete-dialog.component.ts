import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsOrder } from '../aps-order.model';
import { ApsOrderService } from '../service/aps-order.service';

@Component({
  standalone: true,
  templateUrl: './aps-order-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsOrderDeleteDialogComponent {
  apsOrder?: IApsOrder;

  constructor(
    protected apsOrderService: ApsOrderService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
