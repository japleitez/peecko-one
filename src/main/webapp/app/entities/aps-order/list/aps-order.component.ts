import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { AsyncPipe, NgIf } from '@angular/common';
import { NgbInputDatepicker, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { APS_ORDER_USER_ACCESS, ApsOrderAccess, IApsOrder, IApsOrderInfo } from '../aps-order.model';
import { ApsOrderService, EntityInfoArrayResponseType } from '../service/aps-order.service';
import { ApsOrderDeleteDialogComponent } from '../delete/aps-order-delete-dialog.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CustomerService } from '../../customer/service/customer.service';
import { CustomerSelectorComponent } from '../../customer/customer-selector/customer-selector.component';
import { ApsPlanData } from '../../aps-plan/service/aps-plan.data';
import { CustomerData } from '../../customer/service/customer.data';
import { MatDialog } from '@angular/material/dialog';
import { ApsOrderMembersComponent } from '../members/aps-order-members.component';
import { FaIconComponent, FontAwesomeModule } from '@fortawesome/angular-fontawesome';

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
    NgIf,
    NgbInputDatepicker,
    FaIconComponent,
    FontAwesomeModule,
  ]
})
export class ApsOrderComponent implements OnInit {
  ua: ApsOrderAccess = this.getPlanOrderAccess();

  // list controls
  apsOrders?: IApsOrderInfo[];
  isLoading: boolean = false;

  predicate: string = 'id';
  ascending: boolean = true;

  loadAction: string = '';
  REFRESH: string = 'REFRESH';
  BATCH_GENERATE: string = 'BATCH_GENERATE';

  // search fields
  customer: string | null | undefined = null;
  contract: string | null | undefined = null;
  period: string | null | undefined = null;
  starts: string | null | undefined = null;
  ends: string | null | undefined = null;

  constructor(
    protected apsOrderService: ApsOrderService,
    protected customerService: CustomerService,
    protected apsPlanData: ApsPlanData,
    protected customerData: CustomerData,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    public dialog: MatDialog,
  ) {
    this.contract = this.apsPlanData.getContract();
    this.customer = this.customerData.getCode();
  }

  trackId = (_index: number, item: IApsOrder): number => this.apsOrderService.getApsOrderIdentifier(item);

  ngOnInit(): void {
    this.isLoading = true;
    this.refresh();
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
      queryObject.period = this.period;
    } else {
      if (this.customer) {
        queryObject.customer = this.customer;
      }
      if (this.contract) {
        queryObject.contract = this.contract;
      }
      if (this.period) {
        queryObject.period = this.period;
      }
      if (this.starts) {
        queryObject.starts = this.starts;
      }
      if (this.ends) {
        queryObject.ends = this.ends;
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

  protected uploadMembers(o: IApsOrderInfo): void {
    const dialogRef = this.dialog.open(ApsOrderMembersComponent, {
      data: o,
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }

  previousState(): void {
    window.history.back();
  }

}
