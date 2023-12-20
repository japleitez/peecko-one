import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsPricing } from '../aps-pricing.model';
import { ApsPricingService } from '../service/aps-pricing.service';

@Component({
  standalone: true,
  templateUrl: './aps-pricing-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsPricingDeleteDialogComponent {
  apsPricing?: IApsPricing;

  constructor(
    protected apsPricingService: ApsPricingService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsPricingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
