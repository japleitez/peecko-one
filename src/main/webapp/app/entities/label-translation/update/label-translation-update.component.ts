import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LanguageService } from 'app/entities/language/service/language.service';
import { ILabelTranslation, LABEL_ACCESS, LabelAccess } from '../label-translation.model';
import { LabelTranslationService } from '../service/label-translation.service';
import { LabelTranslationFormService, LabelTranslationFormGroup } from './label-translation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-label-translation-update',
  templateUrl: './label-translation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LabelTranslationUpdateComponent implements OnInit {
  ua: LabelAccess = this.getLabelAccess();
  isSaving = false;
  labelTranslation: ILabelTranslation | null = null;
  languageValues: string[] = [];

  editForm: LabelTranslationFormGroup = this.labelTranslationFormService.createLabelTranslationFormGroup(undefined, this.getLabelAccess());

  constructor(
    protected labelTranslationService: LabelTranslationService,
    protected labelTranslationFormService: LabelTranslationFormService,
    protected languageService: LanguageService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ labelTranslation }) => {
      this.labelTranslation = labelTranslation;
      if (labelTranslation) {
        this.updateForm(labelTranslation);
      }

      this.loadLanguageCodes();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const labelTranslation = this.labelTranslationFormService.getLabelTranslation(this.editForm);
    if (labelTranslation.id !== null) {
      this.subscribeToSaveResponse(this.labelTranslationService.update(labelTranslation));
    } else {
      this.subscribeToSaveResponse(this.labelTranslationService.create(labelTranslation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILabelTranslation>>): void {
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

  protected updateForm(labelTranslation: ILabelTranslation): void {
    this.labelTranslation = labelTranslation;
    this.labelTranslationFormService.resetForm(this.editForm, labelTranslation);
  }

  protected loadLanguageCodes(): void {
    this.languageService
      .queryActiveCodes()
      .subscribe(codes => (this.languageValues = this.languageService.addLanguageCodeIfMissing(codes, this.labelTranslation?.lang)));
  }

  protected getLabelAccess(): LabelAccess {
    return LABEL_ACCESS;
  }
}
