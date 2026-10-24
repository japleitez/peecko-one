import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LabelTranslationService } from '../service/label-translation.service';

import { LabelTranslationComponent } from './label-translation.component';
import SpyInstance = jest.SpyInstance;

describe('LabelTranslation Management Component', () => {
  let comp: LabelTranslationComponent;
  let fixture: ComponentFixture<LabelTranslationComponent>;
  let service: LabelTranslationService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'label-translation', component: LabelTranslationComponent }]),
        HttpClientTestingModule,
        LabelTranslationComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(LabelTranslationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LabelTranslationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LabelTranslationService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.labelTranslations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to labelTranslationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getLabelTranslationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getLabelTranslationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should load a page', () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.navigateToWithComponentValues();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['name,asc'],
        }),
      }),
    );
  });

  it('should send the filters to the backend', () => {
    // GIVEN
    comp.filterCode = ' menu ';
    comp.filterLang = 'FR';
    comp.filterText = 'accueil';

    // WHEN
    comp['queryBackend'](1, 'id', true).subscribe();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ code: 'menu', lang: 'FR', text: 'accueil' }));
  });

  it('should not send empty filters to the backend', () => {
    // GIVEN
    comp.filterCode = '  ';
    comp.filterLang = null;
    comp.filterText = '';

    // WHEN
    comp['queryBackend'](1, 'id', true).subscribe();

    // THEN
    const queryObject = (service.query as jest.Mock).mock.lastCall![0];
    expect(queryObject).not.toHaveProperty('code');
    expect(queryObject).not.toHaveProperty('lang');
    expect(queryObject).not.toHaveProperty('text');
  });

  it('should go back to the first page and keep the filters in the URL when a filter changes', () => {
    // GIVEN
    comp.page = 3;
    comp.filterCode = 'menu';
    comp.filterLang = 'DE';
    comp.filterText = 'start';

    // WHEN
    comp.onFilterChange();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({ page: 1, code: 'menu', lang: 'DE', text: 'start' }),
      }),
    );
  });

  it('should remove the filters from the URL when they are cleared', () => {
    // GIVEN
    comp.filterCode = 'menu';
    comp.filterLang = 'EN';
    comp.filterText = 'home';

    // WHEN
    comp.clearFilters();

    // THEN
    expect(comp.hasFilters()).toBe(false);
    const navigationExtras = routerNavigateSpy.mock.lastCall![1];
    expect(navigationExtras?.queryParams).not.toHaveProperty('code');
    expect(navigationExtras?.queryParams).not.toHaveProperty('lang');
    expect(navigationExtras?.queryParams).not.toHaveProperty('text');
  });
});
