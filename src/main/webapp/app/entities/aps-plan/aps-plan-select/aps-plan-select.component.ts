import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, tap } from 'rxjs';
import { AgencyArrayResponseType } from '../../agency/service/agency.service';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { IApsPlan } from '../aps-plan.model';
import { ApsPlanService } from '../service/aps-plan.service';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';

@Component({
  selector: 'jhi-aps-plan-select',
  standalone: true,
  imports: [AsyncPipe, MatAutocompleteModule, MatFormFieldModule, MatInputModule, MatOptionModule, ReactiveFormsModule],
  templateUrl: './aps-plan-select.component.html',
  styleUrl: './aps-plan-select.component.scss',
})
export class ApsPlanSelectComponent implements OnInit {
  @Input() control: FormControl<IApsPlan | string | null> = new FormControl('');
  @Input() cl: string = 'w-20';
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  plans: IApsPlan[] = [];
  filteredPlans!: Observable<IApsPlan[]>;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;
  @Input() required!: boolean;

  constructor(protected apsPlanService: ApsPlanService) {}

  displayName(agency: IApsPlan | null): string {
    return agency?.contract ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.plans, plan => plan.contract);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.apsPlanService
      .queryTrialActive()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: AgencyArrayResponseType) => {
          this.plans = res.body ?? [];
          this.filteredPlans = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.contract;
              return name ? this._filter(name) : this.plans.slice();
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

  private _filter(name: string): IApsPlan[] {
    const value = name.toUpperCase();
    return this.plans.filter(agency => agency.contract?.toUpperCase().includes(value));
  }
}
