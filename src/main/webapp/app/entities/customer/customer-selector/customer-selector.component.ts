import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { ICustomer } from '../customer.model';
import { Observable, tap } from 'rxjs';
import { CustomerService, CustomerArrayResponseType } from '../service/customer.service';
import { map, startWith } from 'rxjs/operators';
import { resolveTypedSelection, selectionRequiredValidator } from 'app/shared/autocomplete/autocomplete-selection';
import { AsyncPipe, NgIf } from '@angular/common';
import { MatAutocompleteModule, MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
@Component({
  selector: 'jhi-customer-selector',
  standalone: true,
  imports: [AsyncPipe, ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './customer-selector.component.html',
  styleUrl: './customer-selector.component.scss',
})
export class CustomerSelectorComponent implements OnInit {
  @Input() control: FormControl<ICustomer | string | null> = new FormControl('');
  @Input() cl: string = 'w-20';
  @Input() set disabled(value: boolean) {
    this.disableControl(value);
  }
  @Input() set required(value: boolean) {
    this._required = value;
  }

  _required: boolean = true;

  public isLoading: boolean = false;
  customers: ICustomer[] = [];
  filteredCustomers!: Observable<ICustomer[]>;
  @ViewChild(MatAutocompleteTrigger) autocompleteTrigger?: MatAutocompleteTrigger;

  constructor(protected customerService: CustomerService) {}

  displayName(cst: ICustomer | null): string {
    return cst?.name ?? '';
  }

  onBlur(): void {
    // while the panel is open the user may be clicking an option; the (closed) handler resolves the text instead
    if (!this.autocompleteTrigger?.panelOpen) {
      this.resolveTypedText();
    }
  }

  resolveTypedText(): void {
    resolveTypedSelection(this.control, this.customers, customer => customer.name);
  }

  ngOnInit(): void {
    this.control.addValidators(selectionRequiredValidator);
    this.control.updateValueAndValidity({ emitEvent: false });
    this._loadOptions();
  }

  private _loadOptions(): void {
    this.isLoading = true;
    this.customerService
      .queryActive()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe({
        next: (res: CustomerArrayResponseType) => {
          this.customers = res.body ?? [];
          this.filteredCustomers = this.control.valueChanges.pipe(
            startWith(''),
            map(value => {
              const name = typeof value === 'string' ? value : value?.name;
              return name ? this._filterCustomers(name) : this.customers.slice();
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

  private _filterCustomers(name: string): ICustomer[] {
    const value = name.toLowerCase();
    return this.customers.filter(c => c.name?.toLowerCase().includes(value));
  }
}
