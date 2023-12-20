import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ApsMembershipService } from '../service/aps-membership.service';

import { ApsMembershipComponent } from './aps-membership.component';

describe('ApsMembership Management Component', () => {
  let comp: ApsMembershipComponent;
  let fixture: ComponentFixture<ApsMembershipComponent>;
  let service: ApsMembershipService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'aps-membership', component: ApsMembershipComponent }]),
        HttpClientTestingModule,
        ApsMembershipComponent,
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
      .overrideTemplate(ApsMembershipComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsMembershipComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApsMembershipService);

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
    expect(comp.apsMemberships?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to apsMembershipService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getApsMembershipIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getApsMembershipIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
