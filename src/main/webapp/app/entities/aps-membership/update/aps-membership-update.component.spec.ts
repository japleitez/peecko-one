import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApsOrder } from 'app/entities/aps-order/aps-order.model';
import { ApsOrderService } from 'app/entities/aps-order/service/aps-order.service';
import { ApsMembershipService } from '../service/aps-membership.service';
import { IApsMembership } from '../aps-membership.model';
import { ApsMembershipFormService } from './aps-membership-form.service';

import { ApsMembershipUpdateComponent } from './aps-membership-update.component';

describe('ApsMembership Management Update Component', () => {
  let comp: ApsMembershipUpdateComponent;
  let fixture: ComponentFixture<ApsMembershipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsMembershipFormService: ApsMembershipFormService;
  let apsMembershipService: ApsMembershipService;
  let apsOrderService: ApsOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsMembershipUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApsMembershipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsMembershipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsMembershipFormService = TestBed.inject(ApsMembershipFormService);
    apsMembershipService = TestBed.inject(ApsMembershipService);
    apsOrderService = TestBed.inject(ApsOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApsOrder query and add missing value', () => {
      const apsMembership: IApsMembership = { id: 456 };
      const apsOrder: IApsOrder = { id: 21857 };
      apsMembership.apsOrder = apsOrder;

      const apsOrderCollection: IApsOrder[] = [{ id: 869 }];
      jest.spyOn(apsOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: apsOrderCollection })));
      const additionalApsOrders = [apsOrder];
      const expectedCollection: IApsOrder[] = [...additionalApsOrders, ...apsOrderCollection];
      jest.spyOn(apsOrderService, 'addApsOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apsMembership });
      comp.ngOnInit();

      expect(apsOrderService.query).toHaveBeenCalled();
      expect(apsOrderService.addApsOrderToCollectionIfMissing).toHaveBeenCalledWith(
        apsOrderCollection,
        ...additionalApsOrders.map(expect.objectContaining),
      );
      expect(comp.apsOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apsMembership: IApsMembership = { id: 456 };
      const apsOrder: IApsOrder = { id: 7007 };
      apsMembership.apsOrder = apsOrder;

      activatedRoute.data = of({ apsMembership });
      comp.ngOnInit();

      expect(comp.apsOrdersSharedCollection).toContain(apsOrder);
      expect(comp.apsMembership).toEqual(apsMembership);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsMembership>>();
      const apsMembership = { id: 123 };
      jest.spyOn(apsMembershipFormService, 'getApsMembership').mockReturnValue(apsMembership);
      jest.spyOn(apsMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsMembership }));
      saveSubject.complete();

      // THEN
      expect(apsMembershipFormService.getApsMembership).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsMembershipService.update).toHaveBeenCalledWith(expect.objectContaining(apsMembership));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsMembership>>();
      const apsMembership = { id: 123 };
      jest.spyOn(apsMembershipFormService, 'getApsMembership').mockReturnValue({ id: null });
      jest.spyOn(apsMembershipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsMembership: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsMembership }));
      saveSubject.complete();

      // THEN
      expect(apsMembershipFormService.getApsMembership).toHaveBeenCalled();
      expect(apsMembershipService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsMembership>>();
      const apsMembership = { id: 123 };
      jest.spyOn(apsMembershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsMembership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsMembershipService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApsOrder', () => {
      it('Should forward to apsOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apsOrderService, 'compareApsOrder');
        comp.compareApsOrder(entity, entity2);
        expect(apsOrderService.compareApsOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
