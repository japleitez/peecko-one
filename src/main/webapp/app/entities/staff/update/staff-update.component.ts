import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgency } from 'app/entities/agency/agency.model';
import { AgencyService } from 'app/entities/agency/service/agency.service';
import { IStaff, STAFF_ACCESS, StaffAccess } from '../staff.model';
import { StaffService } from '../service/staff.service';
import { StaffFormService, StaffFormGroup } from './staff-form.service';

@Component({
  standalone: true,
  selector: 'jhi-staff-update',
  templateUrl: './staff-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StaffUpdateComponent implements OnInit {
  ua: StaffAccess = this.getStaffAccess();
  isSaving = false;
  staff: IStaff | null = null;

  agenciesSharedCollection: IAgency[] = [];

  editForm: StaffFormGroup = this.staffFormService.createStaffFormGroup(undefined, this.getStaffAccess());

  constructor(
    protected staffService: StaffService,
    protected staffFormService: StaffFormService,
    protected agencyService: AgencyService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAgency = (o1: IAgency | null, o2: IAgency | null): boolean => this.agencyService.compareAgency(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staff }) => {
      this.staff = staff;
      if (staff) {
        this.updateForm(staff);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staff = this.staffFormService.getStaff(this.editForm);
    if (staff.id !== null) {
      this.subscribeToSaveResponse(this.staffService.update(staff));
    } else {
      this.subscribeToSaveResponse(this.staffService.create(staff));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaff>>): void {
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

  protected updateForm(staff: IStaff): void {
    this.staff = staff;
    this.staffFormService.resetForm(this.editForm, staff);

    this.agenciesSharedCollection = this.agencyService.addAgencyToCollectionIfMissing<IAgency>(this.agenciesSharedCollection, staff.agency);
  }

  protected loadRelationshipsOptions(): void {
    this.agencyService
      .query()
      .pipe(map((res: HttpResponse<IAgency[]>) => res.body ?? []))
      .pipe(map((agencies: IAgency[]) => this.agencyService.addAgencyToCollectionIfMissing<IAgency>(agencies, this.staff?.agency)))
      .subscribe((agencies: IAgency[]) => (this.agenciesSharedCollection = agencies));
  }

  protected getStaffAccess(): StaffAccess {
    return STAFF_ACCESS;
  }

}
