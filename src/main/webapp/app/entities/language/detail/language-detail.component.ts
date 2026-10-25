import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LANGUAGE_ACCESS, LanguageAccess, ILanguage } from '../language.model';

@Component({
  standalone: true,
  selector: 'jhi-language-detail',
  templateUrl: './language-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LanguageDetailComponent {
  ua: LanguageAccess = LANGUAGE_ACCESS;
  @Input() language: ILanguage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
