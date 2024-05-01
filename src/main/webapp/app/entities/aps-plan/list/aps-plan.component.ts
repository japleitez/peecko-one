import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbInputDatepicker, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { APS_PLAN_ACCESS, ApsPlanAccess, IApsPlan } from '../aps-plan.model';
import { EntityArrayResponseType, ApsPlanService } from '../service/aps-plan.service';
import { ApsPlanDeleteDialogComponent } from '../delete/aps-plan-delete-dialog.component';
import { NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { PlanState } from '../../enumerations/plan-state.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT, DATE_TIME_FORMAT } from '../../../config/input.constants';
import { CustomerData } from '../../customer/service/customer.data';
import { ApsPlanData } from '../service/aps-plan.data';

@Component({
  standalone: true,
  selector: 'jhi-aps-plan',
  templateUrl: './aps-plan.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    NgIf,
    MatInputModule,
    ReactiveFormsModule,
    NgbInputDatepicker
  ]
})
export class ApsPlanComponent implements OnInit {
  ua: ApsPlanAccess = this.getApsPlanUserAccess();
  apsPlans?: IApsPlan[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  searchForm!: FormGroup;

  pCustomerCode: string | null | undefined = null;
  pContract: string | null | undefined = null;

  stateValues: string[] = Object.keys(PlanState);

  constructor(
    protected apsPlanService: ApsPlanService,
    protected customerData: CustomerData,
    protected apsPlanData: ApsPlanData,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
  ) {}

  trackId = (_index: number, item: IApsPlan): number => this.apsPlanService.getApsPlanIdentifier(item);

  ngOnInit(): void {
    this.customerData.getValue().subscribe({ next: c => this.pCustomerCode = c.code });
    this._initForm();
    this.load();
  }

  private _initForm(): void {
    this.searchForm = this.fb.group({
      'customerCode': this.pCustomerCode,
      'contract': null,
      'state': null,
      'starts': null,
      'ends': null,
    });
  }

  navToApsOrder(a: IApsPlan) {
    this.apsPlanData.setValue(a);
    this.router.navigate(['/aps-order'], {
      relativeTo: this.activatedRoute.parent,
    });
  }

  delete(apsPlan: IApsPlan): void {
    const modalRef = this.modalService.open(ApsPlanDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apsPlan = apsPlan;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
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

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.apsPlans = this.refineData(dataFromBody);
  }

  protected refineData(data: IApsPlan[]): IApsPlan[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IApsPlan[] | null): IApsPlan[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    let customerCode = this.searchForm.controls['customerCode'].value;
    if (customerCode) {
      queryObject.customerCode = customerCode;
    }
    let contract = this.searchForm.controls['contract'].value;
    if (contract) {
      queryObject.contract = contract;
    }
    let state = this.searchForm.controls['state'].value;
    if (state) {
      queryObject.state = state;
    }
    let starts = this.searchForm.controls['starts'].value
    if (starts) {
      queryObject.starts = dayjs(starts, DATE_FORMAT).format('YYYY-MM-DD');
    }
    let ends = this.searchForm.controls['ends'].value
    if (ends) {
      queryObject.ends = dayjs(ends, DATE_FORMAT).format('YYYY-MM-DD');
    }
    return this.apsPlanService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  protected getApsPlanUserAccess(): ApsPlanAccess {
    return APS_PLAN_ACCESS;
  }

}
