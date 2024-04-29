import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Observable, tap } from 'rxjs';
import { AgencyArrayResponseType } from '../../agency/service/agency.service';
import { map, startWith } from 'rxjs/operators';
import { IApsPlan } from '../aps-plan.model';
import { ApsPlanService } from '../service/aps-plan.service';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';

@Component({
  selector: 'aps-plan-select',
  standalone: true,
  imports: [
    AsyncPipe,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    ReactiveFormsModule
  ],
  templateUrl: './aps-plan-select.component.html',
  styleUrl: './aps-plan-select.component.scss'
})
export class ApsPlanSelectComponent {
  @Input() control: FormControl<IApsPlan | string | null> = new FormControl('');
  @Input() cl: string = 'w-20';
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  plans!: IApsPlan[];
  filteredPlans!: Observable<IApsPlan[]>;
  @Input() required!: boolean;


  constructor(protected apsPlanService: ApsPlanService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.apsPlanService.queryTrialActive().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: AgencyArrayResponseType) => {
        this.plans = res.body ?? [];
        this.filteredPlans = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.contract;
            return name ? this._filter(name as string) : this.plans.slice();
          }),
        );
      },
    });
  }

  private disableControl(value: boolean) {
    if (value) {
      this.control.disable({ emitEvent: false});
    } else {
      this.control.enable({ emitEvent: false });
    }
  }

  displayName(agency: IApsPlan): string {
    return agency && agency.contract? agency.contract : '';
  }

  private _filter(name: string): IApsPlan[] {
    const value = name.toUpperCase();
    return this.plans.filter(agency => {
      return agency.contract?.toUpperCase().includes(value);
    });
  }

}
