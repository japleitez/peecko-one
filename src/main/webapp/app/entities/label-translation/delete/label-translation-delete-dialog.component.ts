import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILabelTranslation } from '../label-translation.model';
import { LabelTranslationService } from '../service/label-translation.service';

@Component({
  standalone: true,
  templateUrl: './label-translation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LabelTranslationDeleteDialogComponent {
  labelTranslation?: ILabelTranslation;

  constructor(
    protected labelTranslationService: LabelTranslationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.labelTranslationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
