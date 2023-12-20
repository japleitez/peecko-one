import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsOrderService } from '../service/aps-order.service';

import { ApsOrderComponent } from './aps-order.component';

describe('ApsOrder Management Component', () => {
  let comp: ApsOrderComponent;
  let fixture: ComponentFixture<ApsOrderComponent>;
  let service: ApsOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'aps-order', component: ApsOrderComponent }]),
        HttpClientTestingModule,
        ApsOrderComponent,
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
      .overrideTemplate(ApsOrderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsOrderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApsOrderService);

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
    expect(comp.apsOrders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to apsOrderService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getApsOrderIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getApsOrderIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
