import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ILabelTranslation, LABEL_ACCESS, LabelAccess } from '../label-translation.model';
import { EntityArrayResponseType, LabelTranslationService } from '../service/label-translation.service';
import { LabelTranslationDeleteDialogComponent } from '../delete/label-translation-delete-dialog.component';
import { Language } from 'app/entities/enumerations/language.model';

const CODE_PARAM = 'code';
const LANG_PARAM = 'lang';
const TEXT_PARAM = 'text';

@Component({
  standalone: true,
  selector: 'jhi-label-translation',
  templateUrl: './label-translation.component.html',
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
  ],
})
export class LabelTranslationComponent implements OnInit {
  ua: LabelAccess = this.getLabelAccess();
  labelTranslations?: ILabelTranslation[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  languageValues = Object.keys(Language);
  filterCode = '';
  filterLang: keyof typeof Language | null = null;
  filterText = '';

  constructor(
    protected labelTranslationService: LabelTranslationService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: ILabelTranslation): number => this.labelTranslationService.getLabelTranslationIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  onFilterChange(): void {
    // a new filter starts again from the first page
    this.navigateToPage(1);
  }

  clearFilters(): void {
    this.filterCode = '';
    this.filterLang = null;
    this.filterText = '';
    this.navigateToPage(1);
  }

  hasFilters(): boolean {
    return this.filterCode.trim() !== '' || this.filterLang != null || this.filterText.trim() !== '';
  }

  delete(labelTranslation: ILabelTranslation): void {
    const modalRef = this.modalService.open(LabelTranslationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.labelTranslation = labelTranslation;
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
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
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
    this.filterCode = params.get(CODE_PARAM) ?? '';
    const lang = params.get(LANG_PARAM);
    this.filterLang = lang && lang in Language ? (lang as keyof typeof Language) : null;
    this.filterText = params.get(TEXT_PARAM) ?? '';
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.labelTranslations = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ILabelTranslation[] | null): ILabelTranslation[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
      ...this.getFilterParams(),
    };
    return this.labelTranslationService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
      ...this.getFilterParams(),
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

  protected getFilterParams(): Record<string, string> {
    const params: Record<string, string> = {};
    const code = this.filterCode.trim();
    const text = this.filterText.trim();
    if (code) {
      params[CODE_PARAM] = code;
    }
    if (this.filterLang) {
      params[LANG_PARAM] = this.filterLang;
    }
    if (text) {
      params[TEXT_PARAM] = text;
    }
    return params;
  }

  protected getLabelAccess(): LabelAccess {
    return LABEL_ACCESS;
  }
}
