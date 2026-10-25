import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LanguageService } from 'app/entities/language/service/language.service';
import { AGENCY_USER_ACCESS, AgencyAccess, IAgency } from '../agency.model';
import { AgencyService } from '../service/agency.service';
import { AgencyFormService, AgencyFormGroup } from './agency-form.service';
import { ICustomer } from '../../customer/customer.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { ICountry } from 'app/entities/country/country.model';

@Component({
  standalone: true,
  selector: 'jhi-agency-update',
  templateUrl: './agency-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgencyUpdateComponent implements OnInit {
  ua: AgencyAccess = this.getAgencyUserAccess();
  isSaving = false;
  agency: IAgency | null = null;
  languageValues: string[] = [];
  countries: ICountry[] = [];

  editForm: AgencyFormGroup = this.agencyFormService.createAgencyFormGroup(undefined, this.getAgencyUserAccess());

  constructor(
    protected agencyService: AgencyService,
    protected agencyFormService: AgencyFormService,
    protected countryService: CountryService,
    protected languageService: LanguageService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agency }) => {
      this.agency = agency;
      if (agency) {
        this.updateForm(agency);
      }

      this.loadLanguageCodes();
    });
    this.countryService
      .query({ size: 1000, sort: ['name,asc'] })
      .pipe(map(res => res.body ?? []))
      .subscribe(countries => (this.countries = countries));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agency = this.agencyFormService.getAgency(this.editForm);
    if (agency.id !== null) {
      this.subscribeToSaveResponse(this.agencyService.update(agency));
    } else {
      this.subscribeToSaveResponse(this.agencyService.create(agency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgency>>): void {
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

  protected updateForm(agency: IAgency): void {
    this.agency = agency;
    this.agencyFormService.resetForm(this.editForm, agency);
  }

  protected loadLanguageCodes(): void {
    this.languageService
      .queryActiveCodes()
      .subscribe(codes => (this.languageValues = this.languageService.addLanguageCodeIfMissing(codes, this.agency?.language)));
  }

  protected getAgencyUserAccess(): AgencyAccess {
    return AGENCY_USER_ACCESS;
  }
}
