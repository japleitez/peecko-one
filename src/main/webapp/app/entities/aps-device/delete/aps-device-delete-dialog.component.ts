import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApsDevice } from '../aps-device.model';
import { ApsDeviceService } from '../service/aps-device.service';

@Component({
  standalone: true,
  templateUrl: './aps-device-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApsDeviceDeleteDialogComponent {
  apsDevice?: IApsDevice;

  constructor(
    protected apsDeviceService: ApsDeviceService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apsDeviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
