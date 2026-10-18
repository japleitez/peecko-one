import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IAgency } from '../agency.model';
import { Observable, tap } from 'rxjs';
import { AgencyService, AgencyArrayResponseType } from '../service/agency.service';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'jhi-agency-select',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './agency-select.component.html',
  styleUrl: './agency-select.component.scss',
})
export class AgencySelectComponent implements OnInit {
  @Input() control: FormControl<IAgency | string | null> = new FormControl('');
  @Input() cl: string = 'w-20';
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  agencies: IAgency[] = [];
  filteredAgencies!: Observable<IAgency[]>;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;
  @Input() required!: boolean;

  constructor(protected agencyService: AgencyService) {}

  displayName(agency: IAgency | null): string {
    return agency?.name ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.agencies, agency => agency.name);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.agencyService
      .query()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: AgencyArrayResponseType) => {
          this.agencies = res.body ?? [];
          this.filteredAgencies = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.name;
              return name ? this._filter(name) : this.agencies.slice();
            }),
          );
        },
      });
  }

  private disableControl(value: boolean): void {
    if (value) {
      this.control.disable({ emitEvent: false });
    } else {
      this.control.enable({ emitEvent: false });
    }
  }

  private _filter(name: string): IAgency[] {
    const value = name.toLowerCase();
    return this.agencies.filter(agency => agency.name?.toLowerCase().includes(value));
  }
}
