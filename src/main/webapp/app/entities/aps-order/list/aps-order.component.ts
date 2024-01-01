import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormBuilder, FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { IApsOrder, IApsOrderInfo } from '../aps-order.model';
import { ApsOrderService, EntityInfoArrayResponseType } from '../service/aps-order.service';
import { ApsOrderDeleteDialogComponent } from '../delete/aps-order-delete-dialog.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ICustomer } from '../../customer/customer.model';
import { CustomerService, EntityArrayResponseType } from '../../customer/service/customer.service';
import { currentYearMonth, YearMonthValidator } from '../../../shared/validate/validate.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-order',
  templateUrl: './aps-order.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    AsyncPipe,
  ]
})
export class ApsOrderComponent implements OnInit {
  // search form controls
  customers!: ICustomer[];
  filteredCustomers!: Observable<ICustomer[]>;
  customerCtr = new FormControl<string | ICustomer>('');
  startCtr = new FormControl<string>('');
  endCtr = new FormControl<string>('');

  // list controls
  apsOrders?: IApsOrderInfo[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  loadAction = '';
  REFRESH = 'REFRESH';
  BATCH_GENERATE = 'BATCH_GENERATE';

  constructor(
    protected apsOrderService: ApsOrderService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
  ) {}

  trackId = (_index: number, item: IApsOrder): number => this.apsOrderService.getApsOrderIdentifier(item);

  ngOnInit(): void {
    this._initForm();
    this.refresh();
  }

  /*
  search actions
   */
  private _initForm(): void {
    this.isLoading = true;
    this.startCtr.setValue(currentYearMonth());
    this.startCtr.setValidators([Validators.required, YearMonthValidator]);
    this.endCtr.setValidators([YearMonthValidator]);
    this.customerService.queryActive().pipe(tap(() => (this.isLoading = false))).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.customers = res.body ?? [];
        this.filteredCustomers = this.customerCtr.valueChanges.pipe(
          startWith(''),
          map(value => {
            const name = typeof value === 'string' ? value : value?.name;
            return name ? this._filterCustomers(name as string) : this.customers.slice();
          }),
        );
      },
    });
  }

  displayCustomerName(cst: ICustomer): string {
    return cst && cst.name? cst.name : '';
  }
  private _filterCustomers(name: string): ICustomer[] {
    const value = name.toLowerCase();
    return this.customers.filter(c => {
      return c.name?.toLowerCase().includes(value);
    });
  }

  /*
  list actions
   */
  delete(apsOrder: IApsOrder): void {
    const modalRef = this.modalService.open(ApsOrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apsOrder = apsOrder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityInfoArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  refresh(): void {
    this.loadAction = this.REFRESH;
    this._executeLoad();
  }

  batchGenerate(): void {
    this.loadAction = this.BATCH_GENERATE;
    this._executeLoad();
  }

  private _executeLoad(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityInfoArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityInfoArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending)),
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityInfoArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.apsOrders = this.refineData(dataFromBody);
  }

  protected refineData(data: IApsOrderInfo[]): IApsOrderInfo[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IApsOrderInfo[] | null): IApsOrderInfo[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityInfoArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    let customer = this.customerCtr.value;
    let customerId = typeof customer === 'string' ? null : customer?.id;
    if (customerId) {
      queryObject.customerId = customerId;
    }
    if (this.startCtr.value) {
      queryObject.startYearMonth = this.startCtr.value;
    }
    if (this.endCtr.value) {
      queryObject.endYearMonth = this.endCtr.value;
    }
    const executeBatch = (this.loadAction === this.BATCH_GENERATE);
    this.loadAction = this.REFRESH; // reset load action
    if (executeBatch) {
      return this.apsOrderService.batchGenerate(queryObject).pipe(tap(() => (this.isLoading = false)));
    } else {
      return this.apsOrderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
    }
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

}
