import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgencyService } from '../service/agency.service';

import { AgencyComponent } from './agency.component';

describe('Agency Management Component', () => {
  let comp: AgencyComponent;
  let fixture: ComponentFixture<AgencyComponent>;
  let service: AgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'agency', component: AgencyComponent }]), HttpClientTestingModule, AgencyComponent],
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
      .overrideTemplate(AgencyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgencyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AgencyService);

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
    expect(comp.agencies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to agencyService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAgencyIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAgencyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
