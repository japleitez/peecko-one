import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LANGUAGE_ACCESS, LanguageAccess, ILanguage } from '../language.model';
import { LanguageService } from '../service/language.service';
import { LanguageFormService, LanguageFormGroup } from './language-form.service';

@Component({
  standalone: true,
  selector: 'jhi-language-update',
  templateUrl: './language-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LanguageUpdateComponent implements OnInit {
  ua: LanguageAccess = this.getLanguageAccess();
  isSaving = false;
  language: ILanguage | null = null;

  editForm: LanguageFormGroup = this.languageFormService.createLanguageFormGroup(undefined, this.getLanguageAccess());

  constructor(
    protected languageService: LanguageService,
    protected languageFormService: LanguageFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ language }) => {
      this.language = language;
      if (language) {
        this.updateForm(language);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const language = this.languageFormService.getLanguage(this.editForm);
    if (language.id !== null) {
      this.subscribeToSaveResponse(this.languageService.update(language));
    } else {
      this.subscribeToSaveResponse(this.languageService.create(language));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILanguage>>): void {
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

  protected updateForm(language: ILanguage): void {
    this.language = language;
    this.languageFormService.resetForm(this.editForm, language);
  }

  protected getLanguageAccess(): LanguageAccess {
    return LANGUAGE_ACCESS;
  }
}
