import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IApsPlan } from 'app/entities/aps-plan/aps-plan.model';
import { ApsPlanService } from 'app/entities/aps-plan/service/aps-plan.service';
import { ApsOrderService } from '../service/aps-order.service';
import { IApsOrder } from '../aps-order.model';
import { ApsOrderFormService } from './aps-order-form.service';

import { ApsOrderUpdateComponent } from './aps-order-update.component';

describe('ApsOrder Management Update Component', () => {
  let comp: ApsOrderUpdateComponent;
  let fixture: ComponentFixture<ApsOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apsOrderFormService: ApsOrderFormService;
  let apsOrderService: ApsOrderService;
  let apsPlanService: ApsPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ApsOrderUpdateComponent],
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
      .overrideTemplate(ApsOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApsOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apsOrderFormService = TestBed.inject(ApsOrderFormService);
    apsOrderService = TestBed.inject(ApsOrderService);
    apsPlanService = TestBed.inject(ApsPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApsPlan query and add missing value', () => {
      const apsOrder: IApsOrder = { id: 456 };
      const apsPlan: IApsPlan = { id: 15239 };
      apsOrder.apsPlan = apsPlan;

      const apsPlanCollection: IApsPlan[] = [{ id: 28492 }];
      jest.spyOn(apsPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: apsPlanCollection })));
      const additionalApsPlans = [apsPlan];
      const expectedCollection: IApsPlan[] = [...additionalApsPlans, ...apsPlanCollection];
      jest.spyOn(apsPlanService, 'addApsPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apsOrder });
      comp.ngOnInit();

      expect(apsPlanService.query).toHaveBeenCalled();
      expect(apsPlanService.addApsPlanToCollectionIfMissing).toHaveBeenCalledWith(
        apsPlanCollection,
        ...additionalApsPlans.map(expect.objectContaining),
      );
      expect(comp.apsPlansSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apsOrder: IApsOrder = { id: 456 };
      const apsPlan: IApsPlan = { id: 2854 };
      apsOrder.apsPlan = apsPlan;

      activatedRoute.data = of({ apsOrder });
      comp.ngOnInit();

      expect(comp.apsPlansSharedCollection).toContain(apsPlan);
      expect(comp.apsOrder).toEqual(apsOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsOrder>>();
      const apsOrder = { id: 123 };
      jest.spyOn(apsOrderFormService, 'getApsOrder').mockReturnValue(apsOrder);
      jest.spyOn(apsOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsOrder }));
      saveSubject.complete();

      // THEN
      expect(apsOrderFormService.getApsOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(apsOrderService.update).toHaveBeenCalledWith(expect.objectContaining(apsOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsOrder>>();
      const apsOrder = { id: 123 };
      jest.spyOn(apsOrderFormService, 'getApsOrder').mockReturnValue({ id: null });
      jest.spyOn(apsOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apsOrder }));
      saveSubject.complete();

      // THEN
      expect(apsOrderFormService.getApsOrder).toHaveBeenCalled();
      expect(apsOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApsOrder>>();
      const apsOrder = { id: 123 };
      jest.spyOn(apsOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apsOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apsOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApsPlan', () => {
      it('Should forward to apsPlanService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(apsPlanService, 'compareApsPlan');
        comp.compareApsPlan(entity, entity2);
        expect(apsPlanService.compareApsPlan).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
