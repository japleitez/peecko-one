import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsPlanService } from '../service/aps-plan.service';

import { ApsPlanComponent } from './aps-plan.component';

describe('ApsPlan Management Component', () => {
  let comp: ApsPlanComponent;
  let fixture: ComponentFixture<ApsPlanComponent>;
  let service: ApsPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'aps-plan', component: ApsPlanComponent }]),
        HttpClientTestingModule,
        ApsPlanComponent,
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
      .overrideTemplate(ApsPlanComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsPlanComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApsPlanService);

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
    expect(comp.apsPlans?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to apsPlanService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getApsPlanIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getApsPlanIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
