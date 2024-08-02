import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { SortService } from 'app/shared/sort/sort.service';
import { APS_MEMBERSHIP_USER_ACCESS, ApsMembershipAccess, IApsMembership } from '../aps-membership.model';
import { EntityArrayResponseType, ApsMembershipService } from '../service/aps-membership.service';
import { ApsMembershipDeleteDialogComponent } from '../delete/aps-membership-delete-dialog.component';
import { NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { ApsOrderData } from '../../aps-order/aps-order.data';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { IApsOrder } from '../../aps-order/aps-order.model';
import { ApsOrderService } from '../../aps-order/service/aps-order.service';

@Component({
  standalone: true,
  selector: 'jhi-aps-membership',
  templateUrl: './aps-membership.component.html',
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
    FaIconComponent
  ]
})
export class ApsMembershipComponent implements OnInit {
  ua: ApsMembershipAccess = this.getApsMembershipAccess();
  apsMemberships?: IApsMembership[];
  isLoading: boolean = false;

  predicate :string = 'id';
  ascending :boolean = true;

  apsOrderId: number | null | undefined = null;
  apsOrder: IApsOrder | null = null;
  period: number | null | undefined = null;
  contract: string | null | undefined = null;

  constructor(
    protected apsMembershipService: ApsMembershipService,
    protected apsOrderService: ApsOrderService,
    protected apsOrderData: ApsOrderData,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
  ) {
    this.apsOrderId = apsOrderData.getId();
  }

  trackId = (_index: number, item: IApsMembership): number => this.apsMembershipService.getApsMembershipIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(apsMembership: IApsMembership): void {
    const modalRef = this.modalService.open(ApsMembershipDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apsMembership = apsMembership;
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
    if (this.apsOrderId) {
      this.apsOrderService.find(this.apsOrderId)
        .subscribe(resp => {
          this.apsOrder = resp.body;
          if (this.apsOrder) {
            this.period = this.apsOrder?.period;
            this.contract = this.apsOrder?.apsPlan?.contract;
          } else {
            this.period = null;
            this.contract = null;
          }
        });
    }
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
    this.apsMemberships = this.refineData(dataFromBody);
  }

  protected refineData(data: IApsMembership[]): IApsMembership[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IApsMembership[] | null): IApsMembership[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (this.apsOrderId) {
      queryObject.apsOrderId = this.apsOrderId;
    }
    return this.apsMembershipService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  protected getApsMembershipAccess(): ApsMembershipAccess {
    return APS_MEMBERSHIP_USER_ACCESS;
  }

  previousState(): void {
    window.history.back();
  }

}
