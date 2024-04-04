import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { AsyncPipe, NgIf } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import {
  AbstractControl,
  FormBuilder, FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule, ValidationErrors, ValidatorFn,
  Validators
} from '@angular/forms';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { APS_ORDER_USER_ACCESS, ApsOrderAccess, IApsOrder, IApsOrderInfo } from '../aps-order.model';
import { ApsOrderService, EntityInfoArrayResponseType } from '../service/aps-order.service';
import { ApsOrderDeleteDialogComponent } from '../delete/aps-order-delete-dialog.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ICustomer } from '../../customer/customer.model';
import { CustomerService, CustomerArrayResponseType } from '../../customer/service/customer.service';
import { currentYearMonth, isYearMonth, periodValidator } from '../../../shared/validate/custom-validator.directive';
import { CustomerSelectorComponent } from '../../customer/customer-selector/customer-selector.component';

function searchFormValidator(): ValidatorFn {
  return (c: AbstractControl): ValidationErrors | null => {
    let start = c.get('start')?.value;
    if (!start) {
      return { invalidForm: true };
    }
    let end = c.get('end')?.value;
    if (start && end) {
      if (end < start) {
        return { invalidForm: true };
      }
    }
    let customer = c.get('customer')?.value;
    if (!customer && end) {
      return { invalidForm: true };
    }
    return null;
  }
}

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
    CustomerSelectorComponent,
    NgIf
  ]
})
export class ApsOrderComponent implements OnInit {
  ua: ApsOrderAccess = this.getPlanOrderAccess();

  // search form controls
  searchForm!: FormGroup;

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
    this.isLoading = true;
    this._initForm();
    this.refresh();
  }

  /*
  search actions
   */
  private _initForm(): void {
    this.searchForm = this.fb.group({
      'customer': [''],
      'start': [currentYearMonth(), Validators.compose([Validators.required, periodValidator()])],
      'end': [null, periodValidator()],
    }, { validators: [searchFormValidator()] });
  }

  customerControl() {
    return this.searchForm.get('customer') as FormControl<ICustomer | string | null>;
  }

  isStartYearMonthValid(): boolean {
    return this.searchForm.controls['start'].valid;
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
    const executeBatch = (this.loadAction === this.BATCH_GENERATE);
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (executeBatch) {
      queryObject.period = this.searchForm.controls['start'].value;
    } else {
      let customer = this.searchForm.controls['customer'].value;
      let customerId = typeof customer === 'string' ? null : customer?.id;
      if (customerId) {
        queryObject.customerId = customerId;
      }
      if (this.searchForm.controls['start'].value) {
        queryObject.startYearMonth = this.searchForm.controls['start'].value;
      }
      if (this.searchForm.controls['end'].value) {
        queryObject.endYearMonth = this.searchForm.controls['end'].value;
      }
    }
    this.loadAction = this.REFRESH; // reset load action
    if (executeBatch) {
      return this.apsOrderService.batchGenerate(queryObject).pipe(tap(() => (this.isLoading = false)));
    } else {
      return this.apsOrderService.queryInfo(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  protected getPlanOrderAccess(): ApsOrderAccess {
    return APS_ORDER_USER_ACCESS;
  }

}
