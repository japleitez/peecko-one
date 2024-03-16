import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { CONTACT_USER_ACCESS, ContactAccess, IContact } from '../contact.model';
import { NgIf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-contact-detail',
  templateUrl: './contact-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, NgIf]
})
export class ContactDetailComponent {
  ua: ContactAccess = CONTACT_USER_ACCESS;
  @Input() contact: IContact | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
