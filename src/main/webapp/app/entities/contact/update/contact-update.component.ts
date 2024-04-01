import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ContactType } from 'app/entities/enumerations/contact-type.model';
import { ContactService } from '../service/contact.service';
import { CONTACT_USER_ACCESS, ContactAccess, IContact } from '../contact.model';
import { ContactFormService, ContactFormGroup } from './contact-form.service';
import { NgIf } from '@angular/common';
import { CustomerSelectorComponent } from '../../customer/customer-selector/customer-selector.component';

@Component({
  standalone: true,
  selector: 'jhi-contact-update',
  templateUrl: './contact-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgIf, CustomerSelectorComponent]
})
export class ContactUpdateComponent implements OnInit {
  ua: ContactAccess = this.getContactUserAccess();
  isSaving = false;
  contact: IContact | null = null;
  contactTypeValues = Object.keys(ContactType);

  customersSharedCollection: ICustomer[] = [];

  editForm: ContactFormGroup = this.contactFormService.createContactFormGroup(undefined, this.getContactUserAccess());

  constructor(
    protected contactService: ContactService,
    protected contactFormService: ContactFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contact }) => {
      this.contact = contact;
      if (contact) {
        this.updateForm(contact);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contact = this.contactFormService.getContact(this.editForm);
    if (contact.id !== null) {
      this.subscribeToSaveResponse(this.contactService.update(contact));
    } else {
      this.subscribeToSaveResponse(this.contactService.create(contact));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContact>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contact: IContact): void {
    this.contact = contact;
    this.contactFormService.resetForm(this.editForm, contact);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      contact.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.contact?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }

  protected getContactUserAccess(): ContactAccess {
    return CONTACT_USER_ACCESS;
  }

  fc(name: string) {
    return this.editForm.get(name) as FormControl<ICustomer | string | null>;
  }

}
