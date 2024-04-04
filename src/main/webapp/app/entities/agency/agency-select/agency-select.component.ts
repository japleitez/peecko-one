import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { IAgency } from '../agency.model';
import { Observable, tap } from 'rxjs';
import { AgencyService, AgencyArrayResponseType } from '../service/agency.service';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ICustomer } from '../../customer/customer.model';

@Component({
  selector: 'agency-select',
  standalone: true,
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './agency-select.component.html',
  styleUrl: './agency-select.component.scss'
})
export class AgencySelectComponent {
  @Input() control: FormControl<IAgency | string | null> = new FormControl('');
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  public isLoading: boolean = false;
  agencies!: IAgency[];
  filteredAgencies!: Observable<IAgency[]>;
  @Input() required!: boolean;


  constructor(protected agencyService: AgencyService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.agencyService.query().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: AgencyArrayResponseType) => {
        this.agencies = res.body ?? [];
        console.log('this.agencies:')
        console.log(this.agencies)
        this.filteredAgencies = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.name;
            return name ? this._filterAgencies(name as string) : this.agencies.slice();
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

  displayName(agency: IAgency): string {
    return agency && agency.name? agency.name : '';
  }

  private _filterAgencies(name: string): ICustomer[] {
    const value = name.toLowerCase();
    return this.agencies.filter(agency => {
      return agency.name?.toLowerCase().includes(value);
    });
  }

}
