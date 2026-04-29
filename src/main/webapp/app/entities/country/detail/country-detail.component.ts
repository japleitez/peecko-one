import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { COUNTRY_ACCESS, CountryAccess, ICountry } from '../country.model';

@Component({
  standalone: true,
  selector: 'jhi-country-detail',
  templateUrl: './country-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CountryDetailComponent {
  ua: CountryAccess = COUNTRY_ACCESS;
  @Input() country: ICountry | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
