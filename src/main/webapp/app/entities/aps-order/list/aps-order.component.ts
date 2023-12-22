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
import { IApsOrder } from '../aps-order.model';
import { EntityArrayResponseType, ApsOrderService } from '../service/aps-order.service';
import { ApsOrderDeleteDialogComponent } from '../delete/aps-order-delete-dialog.component';

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
  ],
})
export class ApsOrderComponent implements OnInit {
  apsOrders?: IApsOrder[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  loadAction = '';
  DEFAULT_LOAD = 'default-load';
  BATCH_LOAD = 'batch-load';

  constructor(
    protected apsOrderService: ApsOrderService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: IApsOrder): number => this.apsOrderService.getApsOrderIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

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
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadAction = this.DEFAULT_LOAD;
    console.log(this.loadAction)
    this.executeLoad();
  }

  batch(): void {
    this.loadAction = this.BATCH_LOAD;
    console.log(this.loadAction)
    this.executeLoad();
  }

  executeLoad(): void {
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
    this.apsOrders = this.refineData(dataFromBody);
  }

  protected refineData(data: IApsOrder[]): IApsOrder[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IApsOrder[] | null): IApsOrder[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    if (this.loadAction === this.BATCH_LOAD) {
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
