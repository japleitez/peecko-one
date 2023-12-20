import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Language } from 'app/entities/enumerations/language.model';
import { IApsUser } from '../aps-user.model';
import { ApsUserService } from '../service/aps-user.service';
import { ApsUserFormService, ApsUserFormGroup } from './aps-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-user-update',
  templateUrl: './aps-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApsUserUpdateComponent implements OnInit {
  isSaving = false;
  apsUser: IApsUser | null = null;
  languageValues = Object.keys(Language);

  editForm: ApsUserFormGroup = this.apsUserFormService.createApsUserFormGroup();

  constructor(
    protected apsUserService: ApsUserService,
    protected apsUserFormService: ApsUserFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apsUser }) => {
      this.apsUser = apsUser;
      if (apsUser) {
        this.updateForm(apsUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apsUser = this.apsUserFormService.getApsUser(this.editForm);
    if (apsUser.id !== null) {
      this.subscribeToSaveResponse(this.apsUserService.update(apsUser));
    } else {
      this.subscribeToSaveResponse(this.apsUserService.create(apsUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApsUser>>): void {
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

  protected updateForm(apsUser: IApsUser): void {
    this.apsUser = apsUser;
    this.apsUserFormService.resetForm(this.editForm, apsUser);
  }
}
