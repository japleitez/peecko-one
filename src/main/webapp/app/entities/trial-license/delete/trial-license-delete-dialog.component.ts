import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrialLicense } from '../trial-license.model';
import { TrialLicenseService } from '../service/trial-license.service';

@Component({
  standalone: true,
  templateUrl: './trial-license-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrialLicenseDeleteDialogComponent {
  trialLicense?: ITrialLicense;

  constructor(
    protected trialLicenseService: TrialLicenseService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(license: string): void {
    this.trialLicenseService.delete(license).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
