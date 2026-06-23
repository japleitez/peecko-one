import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { CUSTOMER_USER_ACCESS, CustomerAccess, ICustomer } from '../customer.model';
import { CustomerArrayResponseType, CustomerService } from '../service/customer.service';
import { CustomerDeleteDialogComponent } from '../delete/customer-delete-dialog.component';
import { NgIf } from '@angular/common';
import { CustomerData } from '../service/customer.data';
import { ClipboardService } from '../../../shared/common/clipboard.service';
import { MatInputModule } from '@angular/material/input';
import { CustomerState } from '../../enumerations/customer-state.model';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';


@Component({
  standalone: true,
  selector: 'jhi-customer',
  templateUrl: './customer.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    NgIf,
    MatInputModule,
    ReactiveFormsModule,
    FaIconComponent
  ]
})
export class CustomerComponent implements OnInit {
  ua: CustomerAccess = this.getCustomerUserAccess();
  customers?: ICustomer[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  code: string | null | undefined = null;
  name: string | null | undefined = null;
  state: string | null | undefined = null;
  customerStateValues = Object.keys(CustomerState);


  constructor(
    protected customerService: CustomerService,
    protected customerData: CustomerData,
    protected clipboardService: ClipboardService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: ICustomer): number => this.customerService.getCustomerIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  resetSearchForm() {
    this.code = null;
    this.name = null;
    this.state = null;
  }

  navToContacts(c: ICustomer): void {
    this.customerData.setCode(c.code);
    this.router.navigate(['/contact'], {
      relativeTo: this.activatedRoute.parent,
    });
  }

  navToApsPlans(c: ICustomer): void {
    this.customerData.setCode(c.code);
    this.router.navigate(['/aps-plan'], {
      relativeTo: this.activatedRoute.parent,
    });
  }

  navToApsOrders(c: ICustomer): void {
    this.customerData.setCode(c.code);
    this.router.navigate(['/aps-order'], {
      relativeTo: this.activatedRoute.parent,
    });
  }

  delete(customer: ICustomer): void {
    const modalRef = this.modalService.open(CustomerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customer = customer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: CustomerArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: CustomerArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<CustomerArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending)),
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: CustomerArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.customers = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ICustomer[] | null): ICustomer[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<CustomerArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (this.code) {
      queryObject.code = this.code;
    }
    if (this.name) {
      queryObject.name = this.name;
    }
    if (this.state) {
      queryObject.state = this.state;
    }
    return this.customerService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
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

  protected getCustomerUserAccess(): CustomerAccess {
    return CUSTOMER_USER_ACCESS;
  }

  protected copy(elemId: string | null | undefined) {
    if (elemId) {
      this.clipboardService.copy('#' + elemId);
    }
  }

  previousState(): void {
    window.history.back();
  }

}
