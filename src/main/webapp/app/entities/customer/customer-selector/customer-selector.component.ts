import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { ICustomer } from '../customer.model';
import { Observable, tap } from 'rxjs';
import { CustomerService, CustomerArrayResponseType } from '../service/customer.service';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe } from '@angular/common';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'customer-selector',
  standalone: true,
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './customer-selector.component.html',
  styleUrl: './customer-selector.component.scss'
})
export class CustomerSelectorComponent {
  @Input() control: FormControl<ICustomer | string | null> = new FormControl('');
  @Input() cl: string = 'w-20';
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }  @Input() set required(value: boolean) {
    this._required = value;
  }

  _required: boolean = true;

  public isLoading: boolean = false;
  customers!: ICustomer[];
  filteredCustomers!: Observable<ICustomer[]>;

  constructor(protected customerService: CustomerService) {}

  ngOnInit(): void {
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.customerService.queryActive().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: CustomerArrayResponseType) => {
        this.customers = res.body ?? [];
        this.filteredCustomers = this.control.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.name;
            return name ? this._filterCustomers(name as string) : this.customers.slice();
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
  displayName(cst: ICustomer): string {
    return cst && cst.name? cst.name : '';
  }

  private _filterCustomers(name: string): ICustomer[] {
    const value = name.toLowerCase();
    return this.customers.filter(c => {
      return c.name?.toLowerCase().includes(value);
    });
  }

}
